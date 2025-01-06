import numpy as np
import pandas as pd


filepath = 'D:\\Desktop\\licenta_research\\testing_datasets\\androzoo\\classification\\inputDataAll.csv'

df = pd.read_csv(filepath)
labels = np.array(df['CATEGORY'])
features = df.drop('CATEGORY', axis=1)
features = np.array(features)
print(len(features))
print(len(features[0]))
# filepath = 'D:\\Desktop\\licenta_research\\testing_datasets\\androzoo\\classification\\inputDataMajorityScenario.csv'
#
# df = pd.read_csv(filepath)
# labels = np.array(df['CATEGORY'])
# features = df.drop('CATEGORY', axis=1)
# features = np.array(features)
# print(len(features))
#
# filepath = 'D:\\Desktop\\licenta_research\\testing_datasets\\androzoo\\classification\\inputDataWorstCaseScenario.csv'
#
# df = pd.read_csv(filepath)
# labels = np.array(df['CATEGORY'])
# features = df.drop('CATEGORY', axis=1)
# features = np.array(features)
# print(len(features))
#
#
# filepath = 'D:\\Desktop\\licenta_research\\testing_datasets\\androzoo\\regression\\test4_perm_int_appprop_feat\\inputDataAll.csv'
#
# df = pd.read_csv(filepath)
# labels = np.array(df['CATEGORY'])
# features = df.drop('CATEGORY', axis=1)
# features = np.array(features)
# print(len(features))
#
#
# filepath = 'D:\\Desktop\\licenta_research\\testing_datasets\\androzoo\\regression\\test4_perm_int_appprop_feat\\inputDataAverage.csv'
#
# df = pd.read_csv(filepath)
# labels = np.array(df['CATEGORY'])
# features = df.drop('CATEGORY', axis=1)
# features = np.array(features)
# print(len(features))
