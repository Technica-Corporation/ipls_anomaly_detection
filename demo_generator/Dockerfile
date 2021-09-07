FROM arm64v8/python

WORKDIR "/workspace" 

COPY resources/requirements.txt /workspace
COPY resources/generate_data.py /workspace
COPY resources/anomaly_data.sh /workspace
COPY resources/normal_data.sh /workspace

RUN python3 -m pip install -r requirements.txt

