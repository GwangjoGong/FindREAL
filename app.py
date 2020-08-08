import io
import json

import torch
from skimage import io, color
import numpy as np
from PIL import Image
from flask import Flask, jsonify, request
from xception import Xception


app = Flask(__name__)
model = Xception()
ckpt_dir='log_path/Xception_trained_model.pth'
checkpoint = torch.load(ckpt_dir)
model.load_state_dict(checkpoint['model_state_dict'])
model.eval()


def transform(im):
    gray_image = color.rgb2gray(im)
    gray_image = np.expand_dims(gray_image, -1)
    gray_image = gray_image.transpose((2, 0, 1))
    gray_image = torch.from_numpy(gray_image).view(1,1,128,128)
    return gray_image

def test_im(file):
    im = transform(io.imread(file))
    with torch.no_grad():
        input_image = im.cuda().float()
        # compute output
        output = model(input_image).cpu().numpy()
    return output

@app.route('/predict', methods=['POST'])
def predict():
    if request.method == 'POST':
        file = request.files['file']
        output = test_im(file)
        fake,real = output[0][0]
        return jsonify({'fake':fake,'real':real})


if __name__ == '__main__':
    app.run()