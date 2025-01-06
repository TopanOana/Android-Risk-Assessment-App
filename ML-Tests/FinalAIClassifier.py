import pandas as pd
import numpy as np
from onnxconverter_common import Int64TensorType
from sklearn.ensemble import RandomForestClassifier, GradientBoostingClassifier
import joblib
from xgboost.sklearn import XGBClassifier
from skl2onnx import convert_sklearn, to_onnx
from skl2onnx.common.data_types import FloatTensorType
import onnxruntime as rt


def error_calculation(predictions, actual_values):
    total_error = 0
    for pred, val in zip(predictions, actual_values):
        total_error += (val != pred)
    return total_error


def accuracy(error, predictions):
    return 1 - error / len(predictions)


def getFeaturesAndLabels(filepath):
    df = pd.read_csv(filepath)
    # print(len(df.))
    print(df.shape[1])
    labels = np.array(df['CATEGORY'])
    print(labels[0])
    features = df.drop('CATEGORY', axis=1)
    features = np.array(features)
    print(len(features[0]))
    return features, labels


def getTrainedClassifier(train_features, train_labels, algorithm):
    match algorithm:
        case "rf":
            current_algorithm = RandomForestClassifier(n_estimators=250, max_depth=10)
            current_algorithm.fit(train_features, train_labels)
            return current_algorithm
        case "gb":
            current_algorithm = GradientBoostingClassifier(n_estimators=250, max_depth=10, learning_rate=0.05)
            current_algorithm.fit(train_features, train_labels)

            return current_algorithm
        case other:
            current_algorithm = None
            return current_algorithm


def predictOnTestData(test_features, test_labels, algorithm):
    predictions = algorithm.predict(test_features)
    error = error_calculation(predictions, test_labels)
    print('TEST total error is: ' + str(error))
    print('TEST The accuracy is: ' + str(accuracy(error, predictions)))


def predictOnTrainData(train_features, train_labels, algorithm):
    predictions = algorithm.predict(train_features)
    error = error_calculation(predictions, train_labels)

    print('TRAIN total error is: ' + str(error))
    print('TRAIN The accuracy is: ' + str(accuracy(error, predictions)))


def testDatasetOnGradientBoosting(filepathTrain, filepathTest):
    print("beginning of GRADIENT BOOSTING " + filepathTrain)
    train_features, train_labels = getFeaturesAndLabels(filepathTrain)
    test_features, test_labels = getFeaturesAndLabels(filepathTest)

    gradientBoosting = getTrainedClassifier(train_features, train_labels, "gb")

    predictOnTestData(test_features, test_labels, gradientBoosting)
    predictOnTrainData(train_features, train_labels, gradientBoosting)

    print("end of " + filepathTrain)
    return gradientBoosting


def mainWrite():
    home_directory = 'D:\\Desktop\\licenta_research\\testing_datasets\\androzoo\\classification\\'

    algorithm = testDatasetOnGradientBoosting(
        home_directory + 'inputDataAll.csv',
        home_directory + 'testData.csv'

    )

    joblib.dump(algorithm, home_directory + 'algorithm.joblib')


def mainRead():
    home_directory = 'D:\\Desktop\\licenta_research\\testing_datasets\\androzoo\\classification\\'

    algorithm = joblib.load(home_directory + 'algorithm.joblib')

    test_features, test_labels = getFeaturesAndLabels(home_directory + 'testData.csv')

    print(len(test_features[0]))

    predictOnTestData(test_features, test_labels, algorithm)

    # initial_type = [('int_input', Int64TensorType([None, len(test_features[0])]))]
    # converted_model = convert_sklearn(algorithm, initial_types=initial_type)
    #
    #
    # # onx = to_onnx(algorithm, test_features[:1])
    # with open(home_directory + "algorithm.onnx", "wb") as f:
    #     f.write(converted_model.SerializeToString())


def testONNX():
    home_directory = 'D:\\Desktop\\licenta_research\\testing_datasets\\androzoo\\classification\\'
    test_features, test_labels = getFeaturesAndLabels(home_directory + 'testData.csv')

    sess = rt.InferenceSession(home_directory + "algorithm.onnx")
    # input_name = sess.get_inputs()[0]


# mainWrite()

mainRead()
