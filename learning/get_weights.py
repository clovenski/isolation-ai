#!/usr/bin/env python

import pandas as pd
import numpy as np
from scipy import linalg

def get_weights(X,y):
    weights_list = np.matmul(np.matmul(linalg.pinv(np.matmul(X.T, X)), X.T), y)
    return weights_list

feat_df = pd.read_csv('../dataset/features.txt', sep=' ', header=None)
X = feat_df.values

labels = pd.read_csv('../dataset/labels.txt', header=None).values

print(X.shape, labels.shape)

weights_file = open('weights.txt', mode='w')
weights = get_weights(X, labels)

for weight in np.nditer(weights):
    weights_file.write(str(weight) + '\n')
