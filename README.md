# Android Security Assessment Project

This project was developed with the security of Android device users in mind, combining elements of Machine Learning and Cybersecurity to assess the risks posed by certain applications.

## Overview

The goal of this project is to detect potential security risks arising from application interactions. Specifically, it focuses on scenarios where:
- **Application A (Alice):** Has access to sensitive information (e.g., location).
- **Application B (Bob):** Has access to external communication (e.g., internet).

While both apps may appear harmless individually, together they could enable data leakage. For example, Alice could send your location to Bob, who then forwards it to an attacker, compromising your privacy and security.

## Methodology

### Research Phase
We began by studying:
- The **Android Operating System**, focusing on how applications communicate and access information.
- Possible attack vectors and permissions misuse.

We utilized the [AndroZoo dataset](https://androzoo.uni.lu/), containing over 24 million APKs, as the foundation for our analysis.

### Data Collection
We extracted information from the `AndroidManifest.xml` file of each application, including:
- Permissions.
- Intents.
- Hardware and software features.
- Application properties.

### Feature Engineering
We created multiple feature sets for input into Machine Learning algorithms:
1. **Test 1:** Binary check of whether permissions and intents were declared.
2. **Test 2:** Included binary checks, the number of custom permissions, and the frequency of declared intents.
3. **Test 3:** Added application tag properties extracted from Android Documentation.
4. **Test 4:** Incorporated hardware and software features based on:
   - Required (2).
   - Requested but not required (1).
   - Not requested (0).

### Machine Learning Algorithms
We experimented with various algorithms, including:
- **Random Forest**
- **Gradient Boosting**
- **Extreme Gradient Boosting**
- **Support Vector Machines (SVM)**

#### Key Parameters and Results
- Ensemble learners (e.g., Gradient Boosting) were fine-tuned with different numbers of estimators.
- Both regressors and classifiers were tested.
- Multiple strategies for handling duplicate entries were explored, such as:
  - Averaging the output values.
  - Selecting the most frequent output value.
  - Choosing the highest security risk.

The best performance was achieved using the **Gradient Boosting Classifier** on Test 4 data with 250 estimators, yielding an **accuracy of 80.09%**.

### Grouping Algorithm
We implemented a grouping algorithm to identify sets of inter-communicating apps:
1. Extracted inter-app communication calls from manifests.
2. Grouped apps that communicated with one another.
3. Repeated the grouping process iteratively until no changes occurred.

### Final Integration
The best-performing ML model was integrated into an Android application using ONNX. This application allows users to assess the security risks posed by installed apps and their interactions.

## Conclusion
This project highlights the potential security threats posed by seemingly benign applications and provides a robust tool for detecting and mitigating these risks. 

