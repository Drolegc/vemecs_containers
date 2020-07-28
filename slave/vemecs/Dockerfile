FROM python:3.7-slim
ENV PYTHONUNBUFFERED 1

RUN pip install "python-socketio[client]"
COPY . /usr/src/app
WORKDIR /usr/src/app

CMD [ "python","vemec.py" ]
