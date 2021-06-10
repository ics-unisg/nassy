#!/usr/bin/python3

"""Runs data against the cl model.
"""

__author__ = "Martin Eigenmann"
__license__ = "unlicence"
__version__ = "0.0.1"
__email__ = "martin.eigenmann@unisg.ch"
__status__ = "Prototpye"

import pandas as pd
import sys
import torch
import numpy as np

class NN(torch.nn.Module):
    def __init__(self, input_dimension,number_of_target_classes):
        super().__init__()
        # stack layers
        self.module_list = torch.nn.ModuleList()
        ## stacks contain of linear + nonlinear layers
        self.module_list.append(torch.nn.Linear(input_dimension,20))
        self.module_list.append(torch.nn.Tanh())
        #
        self.module_list.append(torch.nn.Linear(20,20))
        self.module_list.append(torch.nn.Tanh())
        #
        self.module_list.append(torch.nn.Linear(20,10))
        self.module_list.append(torch.nn.Tanh())
        # map to output layer
        self.module_list.append(torch.nn.Linear(10,number_of_target_classes))
    
    def forward(self,x):
        for module in self.module_list:
            x = module(x)
        return x


with torch.no_grad():
    df = pd.read_csv(sys.stdin)

    device = torch.device('cpu')
    model2 = NN(17,2)
    model2.load_state_dict(torch.load('/processors/torch', map_location=device))
    model2.eval()
    data_to_predict = df.drop(['b_start', 'b_duration', 'e_start', 'e_duration', 'e_distance','study','subject'], axis=1).to_numpy()[0]
    prediction = model2(torch.from_numpy(data_to_predict).float())
    predicted_class = np.argmax(prediction)

    b_start = int(df.iloc[0].b_start)
    e_start = int(df.iloc[0].e_start)
    study = df.iloc[0].study
    subject = df.iloc[0].subject

    print(f'prediction_class,b_start,e_start,study,subject')
    print(f'{predicted_class},{b_start},{e_start},{study},{subject}')
