import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestClassifier, GradientBoostingClassifier
from xgboost.sklearn import XGBClassifier
from sklearn.svm import SVC

def getData(filepath):
    df = pd.read_csv(filepath)
    labels = np.array(df['CATEGORY'])
    features = df.drop('CATEGORY', axis=1)
    features = np.array(features)
    return features, labels


def error_calculation(predictions, actual_values):
    total_error = 0
    for pred, val in zip(predictions, actual_values):
        total_error += (val != pred)
    return total_error


def accuracy(error, predictions):
    return 1 - error / len(predictions)


def getFeaturesAndLabels(filepath):
    df = pd.read_csv(filepath)
    labels = np.array(df['CATEGORY'])
    features = df.drop('CATEGORY', axis=1)
    features = np.array(features)
    return features, labels


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
        predict_categ = predictions[i]
        label_categ = labels[i]
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




def trainClassifiers(train_features, train_labels, test_features, test_labels):
    # random forest
    for x in range(100, 251, 50):
        random_forest = RandomForestClassifier(n_estimators=x, max_depth=10)
        random_forest.fit(train_features, train_labels)
        print('RANDOM FOREST NR ESTIMATORS=' + str(x))
        print('train')
        getErrorBasedOnCategory(train_features, train_labels, random_forest)
        print('test')
        getErrorBasedOnCategory(test_features, test_labels, random_forest)
        print('~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~')
    print('~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~')

    # gradient boost
    for x in range(100, 251, 50):
        gradient_boost = GradientBoostingClassifier(n_estimators=x, max_depth=10)
        gradient_boost.fit(train_features, train_labels)
        print('GRADIENT BOOSTING NR ESTIMATORS=' + str(x))
        print('train')
        getErrorBasedOnCategory(train_features, train_labels, gradient_boost)
        print('test')
        getErrorBasedOnCategory(test_features, test_labels, gradient_boost)
        print('~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~')
    print('~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~')
    #xgboost
    for x in range(100, 251, 50):
        xgradient_boost = XGBClassifier(n_estimators=x, max_depth=10)
        xgradient_boost.fit(train_features, train_labels)
        print('XGBoost NR ESTIMATORS=' + str(x))
        print('train')
        getErrorBasedOnCategory(train_features, train_labels, xgradient_boost)
        print('test')
        getErrorBasedOnCategory(test_features, test_labels, xgradient_boost)
        print('~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~')
    print('~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~')
    #svm
    svm = SVC()
    svm.fit(train_features, train_labels)
    print('SVM')
    print('train')
    getErrorBasedOnCategory(train_features, train_labels, svm)
    print('test')
    getErrorBasedOnCategory(test_features, test_labels, svm)
    print('~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~')
    print('~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~')

def main():
    home_directory = 'D:\\Desktop\\licenta_research\\testing_datasets\\androzoo\\classification'
    filepathTest = home_directory + '\\testData.csv'
    test_features, test_labels = getData(filepathTest)
    #all
    print('inputDataAll.csv')
    filepathTrain = home_directory + '\\inputDataAll.csv'
    train_features, train_labels = getData(filepathTrain)
    trainClassifiers(train_features, train_labels, test_features, test_labels)
    #majority
    print('inputDataMajorityScenario.csv')
    filepathTrain = home_directory + '\\inputDataMajorityScenario.csv'
    train_features, train_labels = getData(filepathTrain)
    trainClassifiers(train_features, train_labels, test_features, test_labels)
    #worst case
    print('inputDataWorstCaseScenario.csv')
    filepathTrain = home_directory + '\\inputDataWorstCaseScenario.csv'
    train_features, train_labels = getData(filepathTrain)
    trainClassifiers(train_features, train_labels, test_features, test_labels)



main()
