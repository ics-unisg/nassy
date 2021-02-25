#!/usr/bin/python3
 
import pandas as pd
import sys

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


df = pd.read_csv(sys.stdin, sep=";")
df['Timestamp'] = df.apply(lambda x: int(x['Timestamp'] / 10), axis=1)
df = df.set_index(pd.TimedeltaIndex(df['Timestamp'].values))
df['ET_PubilAvg'] = df.apply(lambda x: (x['ET_PupilLeft'] + x['ET_PupilRight']) / 2, axis=1)


baseline = df[df['type'] == 'B']
b_min, b_max, b_mean, b_median, b_std, b_peak_count, b_peak_count_pm = extract_features(baseline)


experiment = df[df['type'] == 'M']
e_min, e_max, e_mean, e_median, e_std, e_peak_count, e_peak_count_pm = extract_features(experiment)

d_mean = b_mean - e_mean
d_median = b_median - e_median
d_std = b_std - e_std



print(f'd_mean,d_median,d_std,e_min,e_max,e_mean,e_median,e_std,e_peak_count,e_peak_count_pm,b_min,b_max,b_mean,b_median,b_std,b_peak_count,b_peak_count_pm')
print(f'{d_mean},{d_median},{d_std},{e_min},{e_max},{e_mean},{e_median},{e_std},{e_peak_count},{e_peak_count_pm},{b_min},{b_max},{b_mean},{b_median},{b_std},{b_peak_count},{b_peak_count_pm}')

