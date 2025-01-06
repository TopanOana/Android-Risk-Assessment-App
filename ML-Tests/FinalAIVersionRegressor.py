import joblib
import numpy as np
import pandas as pd
from xgboost.sklearn import XGBRegressor
from sklearn.metrics import mean_squared_error as MSE


def getCategory(value):
    if value < 4:
        return 0
    elif value < 8:
        return 1
    else:
        return 2


def getErrorBasedOnCategory(features, labels, algorithm):
    total_error = 0
    predictions = algorithm.predict(features)
    # for i in range(100):
    #     print("prediction =" + str(predictions[i]) + " .... " + "label =" + str(labels[i]))
    count_categ0 = 0
    error_categ0 = 0
    count_categ1 = 0
    error_categ1 = 0
    count_categ2 = 0
    error_categ2 = 0

    for i in range(len(predictions)):
        predict_categ = getCategory(predictions[i])
        label_categ = getCategory(labels[i])
        match label_categ:
            case 0:
                count_categ0 += 1
            case 1:
                count_categ1 += 1
            case 2:
                count_categ2 += 1
        if predict_categ != label_categ:
            total_error += 1
            match label_categ:
                case 0:
                    error_categ0 += 1
                case 1:
                    error_categ1 += 1
                case 2:
                    error_categ2 += 1

    print("error based on category: " + str(total_error))
    print("accuracy based on category: " + str(1 - (total_error / len(predictions))))
    print("\tcategory 0: \n\t\ttotal nr errors = " + str(error_categ0) + "\n\t\t accuracy = " + str(
        1 - (error_categ0 / count_categ0)))
    print("\tcategory 1: \n\t\ttotal nr errors = " + str(error_categ1) + "\n\t\t accuracy = " + str(
        1 - (error_categ1 / count_categ1)))
    print("\tcategory 2: \n\t\ttotal nr errors = " + str(error_categ2) + "\n\t\t accuracy = " + str(
        1 - (error_categ2 / count_categ2)))


def predictOnTrainDataRMSE(train_features, train_labels, algorithm):
    predictions = algorithm.predict(train_features)
    rmse = np.sqrt(MSE(train_labels, predictions))
    # print('TRAIN total error is: ' + str(error))
    print('TRAIN The RMSE is: ' + str(rmse))


def predictOnTestDataRMSE(test_features, test_labels, algorithm):
    predictions = algorithm.predict(test_features)
    rsme = np.sqrt(MSE(test_labels, predictions))
    print('TEST The RMSE is: ' + str(rsme))


def getData(filepath):
    df = pd.read_csv(filepath)
    labels = np.array(df['CATEGORY'])
    features = df.drop('CATEGORY', axis=1)
    features = np.array(features)
    return features, labels


def testDatasetXGBRegressorRMSE(filepathTrain, filepathTest):
    print("beginning of XGBRegressor " + filepathTrain)
    train_features, train_labels = getData(filepathTrain)
    test_features, test_labels = getData(filepathTest)

    algorithm = XGBRegressor(objective='reg:squarederror', n_estimators=200, max_depth=10, seed=100)

    algorithm.fit(train_features, train_labels)

    predictOnTestDataRMSE(test_features, test_labels, algorithm)
    predictOnTrainDataRMSE(train_features, train_labels, algorithm)
    print('~~~~test~~~~~')
    getErrorBasedOnCategory(test_features, test_labels, algorithm)
    print('~~~~train~~~~')
    getErrorBasedOnCategory(train_features, train_labels, algorithm)
    print("end of " + filepathTrain)

    return algorithm


def mainWrite():
    home_directory = 'D:\\Desktop\\licenta_research\\testing_datasets\\androzoo\\regression\\'

    algorithm = testDatasetXGBRegressorRMSE(home_directory + 'inputDataAll.csv', home_directory + 'testData.csv')

    # joblib.dump(algorithm, home_directory + 'algorithm.joblib')


def mainRead():
    home_directory = 'D:\\Desktop\\licenta_research\\testing_datasets\\androzoo\\regression\\'

    algorithm = joblib.load(home_directory + 'algorithm.joblib')

    test_features, test_labels = getData(home_directory + 'testData.csv')

    predictOnTestDataRMSE(test_features, test_labels, algorithm)


# mainRead()

mainWrite()
