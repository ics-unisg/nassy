#!/usr/bin/python3
 
import pandas as pd
import sys
import math, pywt, numpy as np


def modmax(d):
    m = [0.0]*len(d)
    for i in iter(range(len(d))):
        m[i] = math.fabs(d[i])

        t = [0.0]*len(d)
        for i in iter(range(len(d))):
            ll = m[i-1] if i >= 1 else m[i]
            oo = m[i]
            rr = m[i+1] if i < len(d) - 2 else m[i]

            if (ll <= oo and oo >= rr) and (ll < oo or oo > rr):
                t[i] = math.sqrt(d[i]**2)
            else:
                t[i] = 0.0

    return t

def lhipa(d):
    w = pywt.Wavelet('sym16')
    maxlevel = pywt.dwt_max_level(len(d), filter_len=w.dec_len)

    hif, lof = 1, int(maxlevel/2)

    cD_H = pywt.downcoef('d', d, 'sym16', 'per', level=hif)
    cD_L = pywt.downcoef('d', d, 'sym16', 'per', level=lof)

    cD_H[:] = [x/math.sqrt(2**hif) for x in cD_H]
    cD_L[:] = [x/math.sqrt(2**lof) for x in cD_L]

    cD_LH = cD_L
    for i in range(len(cD_L)):
        cD_LH[i] = cD_L[i] / cD_H[int(((2**lof)/(2**hif))*i)]

    cD_LHm = modmax(cD_LH)

    luniv = np.std(cD_LHm) * math.sqrt(2.0*np.log2(len(cD_LHm)))
    cD_LHt = pywt.threshold(cD_LHm, luniv, mode="less")

    tt = 10#d[-1].timestamp() - d[0].timestamp()

    ctr = 0
    for i in iter(range(len(cD_LHt))):
        if math.fabs(cD_LHt[i]) > 0: ctr += 1

    return float(ctr)/tt

df = pd.read_csv(sys.stdin)
df['timestamp'] = df.apply(lambda x: int(x['timestamp'] / 10), axis=1)
df = df.set_index(pd.TimedeltaIndex(df['timestamp'].values))
df['diameter'] = (df['diameter_left'] + df['diameter_right']) / 2

study = df.iloc[0].study
subject = df.iloc[0].subject
index = lhipa(df['diameter'].to_numpy())


print(f'lhipa,timestamp,study,subject')
print(f'{index if index is not None else 0.0},{df.iloc[0]["timestamp"]},{study},{subject}')

