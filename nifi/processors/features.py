#!/usr/bin/python3
 
import pandas as pd
import sys
from copy import copy

def extract_features(df):
    df_min, df_max, df_mean, df_median, df_std = df['ET_PubilAvg'].agg([pd.np.min, pd.np.max, pd.np.mean, pd.np.median, pd.np.std])
    df['rolling_mean'] = df['ET_PubilAvg'].rolling('8s', min_periods=1).mean()
    df_resampled = df[~df.index.duplicated(keep='first')].resample('1ms').pad()
    df_resampled['rolling_mean'] = df_resampled['rolling_mean'].shift(periods=-4000)
    df_resampled['phasic'] = df_resampled['ET_PubilAvg'] - df_resampled['rolling_mean']
    df_resampled['rolling_phasic'] = df_resampled['phasic'].rolling('1s', min_periods=1).mean().shift(periods=-500)
    df_resampled['peaks'] = df_resampled['rolling_phasic'][(df_resampled['rolling_phasic'].shift(1) < df_resampled['rolling_phasic']) & (df_resampled['rolling_phasic'].shift(-1) < df_resampled['rolling_phasic']) & (df_resampled['rolling_phasic'] > 0.1)]
    peak_count = df_resampled['peaks'].dropna().count()

    duration = (df_resampled.index[-1] - df_resampled.index[0]).seconds / 60
    peak_count_pm = peak_count / duration



    return (df_min, df_max, df_mean, df_median, df_std, peak_count, peak_count_pm)


df = pd.read_csv(sys.stdin)
df['timestamp'] = df.apply(lambda x: int(x['timestamp'] / 10), axis=1)
df = df.set_index(pd.TimedeltaIndex(df['timestamp'].values))
df['ET_PubilAvg'] = df.apply(lambda x: (x['diameter_left'] + x['diameter_right']) / 2, axis=1)

study = df.iloc[0].study
subject = df.iloc[0].subject

baseline = copy(df[df['type'] == 'BASELINE'])
baseline = baseline.set_index(pd.TimedeltaIndex(baseline['timestamp'].values))
b_min, b_max, b_mean, b_median, b_std, b_peak_count, b_peak_count_pm = extract_features(baseline)
b_start = baseline.iloc[0]['timestamp']
b_end = baseline.iloc[-1]['timestamp']
b_duration = b_end - b_start

experiment = copy(df[df['type'] == 'EXPERIMENT'])
experiment = experiment.set_index(pd.TimedeltaIndex(experiment['timestamp'].values))
e_min, e_max, e_mean, e_median, e_std, e_peak_count, e_peak_count_pm = extract_features(experiment)
e_start = experiment.iloc[1]['timestamp']
e_end = experiment.iloc[-1]['timestamp']
e_duration = e_end - e_start
e_distance = e_start - b_end

d_mean = b_mean - e_mean
d_median = b_median - e_median
d_std = b_std - e_std



print(f'd_mean,d_median,d_std,e_min,e_max,e_mean,e_median,e_std,e_peak_count,e_peak_count_pm,b_min,b_max,b_mean,b_median,b_std,b_peak_count,b_peak_count_pm,b_start,b_duration,e_start,e_duration,e_distance,study,subject')
print(f'{d_mean},{d_median},{d_std},{e_min},{e_max},{e_mean},{e_median},{e_std},{e_peak_count},{e_peak_count_pm},{b_min},{b_max},{b_mean},{b_median},{b_std},{b_peak_count},{b_peak_count_pm},{b_start},{b_duration},{e_start},{e_duration},{e_distance},{study},{subject}')

