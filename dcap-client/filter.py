import json
import requests
import random, string
import pandas as pd
import io
import time

settings = {
    "authorisation": {
        'username': 'admin@dcap.local',
        'password': 'admin'
    },
    "url": 'http://localhost:8989'
}

def randomword(length):
   letters = string.ascii_lowercase
   return ''.join(random.choice(letters) for i in range(length))

def clean(df):
    with requests.Session() as s:
        # login
        r = s.post(
            url=settings.get('url') + "/cheetah/login", 
            data=settings.get('authorisation')
        )
        # create study
        try:
            data = json.loads(s.post(
                url=settings.get('url') + "/cheetah/api/user/study",
                headers={'content-type': 'application/json'},
                data=json.dumps({
                    'comment': '',
                    'name': randomword(20)
                })
            ).content.decode('UTF-8'))
            study_id = data['resBody']["id"]
        except Exception as e:
            print('Could not create study')
            raise e


        # create subject
        try:
            data = json.loads(s.post(
                url=settings.get('url') + "/cheetah/api/user/addsubject", 
                headers={'content-type': 'application/json'},
                data=json.dumps({
                    'comment': '',
                    'email': f'{randomword(20)}@dcap.local',
                    'id': 0,
                    'studyId': study_id,
                    'studyName': randomword(20),
                    'subject_id': randomword(20),
                    'synchronized_from': 0,
                })
            ).content.decode('UTF-8'))
            subject_id = data['resBody']["id"]
        except Exception as e:
            print('Could not create subject')
            raise e

        # upload data
        try:
            stream = io.StringIO()
            df.to_csv(stream, sep='\t', index=False)

            data = json.loads(s.post(
                url=settings.get('url') + "/cheetah/api/user/uploadFile",
                files=[('files', ('file.tsv', stream.getvalue(), 'application/octet-stream'))],
                data={
                    'subjectIds': [subject_id]
                }
            ).content.decode('UTF-8'))
            file_id = data['resBody']['easyUserDataListSuccessfultMapped'][0]["id"]
        except Exception as e:
            print('Could not upload data')
            raise e

        # filter data
        try:
            """
            Broken Filter
            {
                'name': "BlinkDetectionFilter",
                'actualParameters': {
                    'leftPupilGazeXColumnName': 'ET_GazeLeftx',
                    'leftPupilGazeYColumnName': 'ET_GazeLefty',
                    'rightPupilGazeXColumnName': 'ET_GazeRightx',
                    'rightPupilGazeYColumnName': 'ET_GazeRighty',
                    'blinkDetectionTimeThreashold': 60,
                    'left_pupil': 'ET_PupilLeft',
                    'right_pupil': 'ET_PupilRight',
                    'timestampcolumn': 'Timestamp',
                },
                'columns': {}
            },
            """
            # request filter
            data = json.loads(s.post(
                url=settings.get('url') + "/cheetah/api/user/filterrequest", 
                headers={'content-type': 'application/json'},
                data=json.dumps({
                    "files": [file_id],
                    "filters": [
                        {
                            'name': "SubstitutePupilFilter",
                            'actualParameters': {
                                'left_pupil': 'ET_PupilLeft',
                                'right_pupil': 'ET_PupilRight',
                                'timestampcolumn': 'Timestamp',
                            },
                            'columns': {
                                'left_pupil': 'ET_PupilLeft',
                                'right_pupil': 'ET_PupilRight',
                            }

                        },
                        {
                            'name': "SubstituteGazePointFilter",
                            'actualParameters': {
                                'leftPupilGazeXName': 'ET_GazeLeftx',
                                'leftPupilGazeYName': 'ET_GazeLefty',
                                'rightPupilGazeXName': 'ET_GazeRightx',
                                'rightPupilGazeYName': 'ET_GazeRighty',
                                'timestampcolumn': 'Timestamp',
                            },
                            'columns': {}

                        },
                        {
                            'name': "ButterworthFilter",
                            'actualParameters': {
                                'hertz': 4,
                                'sampleRate': 300,
                                'timestampcolumn': 'Timestamp',
                            },
                            'columns': {
                                'left_pupil': 'ET_PupilLeft',
                                'right_pupil': 'ET_PupilRight',
                            },
                            'decimalSeparator': '.'
                        },
                        {
                            'name': "LinearInterpolationFilter",
                            'actualParameters': {
                                'left_pupil': 'ET_PupilLeft',
                                'right_pupil': 'ET_PupilRight',
                                'timestampcolumn': 'Timestamp',
                            },
                            'columns': {
                                'left_pupil': 'ET_PupilLeft',
                                'right_pupil': 'ET_PupilRight',
                            }
                        }

                    ]
                })
            ).content.decode('UTF-8'))
            task_id = data['resBody']

            # wait for task to finish
            while True:
                data = json.loads(s.get(
                    url=settings.get('url') + "/cheetah/api/user/taskFinished/" + task_id, 
                    headers={'content-type': 'application/json'},
                ).content.decode('UTF-8'))
                if data['resBody'] == True:
                    break
                time.sleep(0.1)

            # get filtered file id
            data = json.loads(s.get(
                url=settings.get('url') + "/cheetah/api/user/taskid/" + task_id, 
            ).content.decode('UTF-8'))
            filtered_file_id = data['resBody']["id"]

            # download filtered data
            data = s.get(
                url=settings.get('url') + "/cheetah/api/user/download/" + str(filtered_file_id), 
            ).content.decode("utf-8")

        except Exception as e:
            print('Could not filter data')
            raise e

        # cleanup
        # TODO: also delete study and subject
        s.delete(
            url=settings.get('url') + "/cheetah/api/user/file/"+str(file_id)
        )
        s.delete(
            url=settings.get('url') + "/cheetah/api/user/file/"+str(filtered_file_id)
        )

        return pd.read_csv(io.StringIO(data), sep='\t')