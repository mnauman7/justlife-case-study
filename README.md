<b>This is a booking application where you can book staff for cleaning services.</b>

Application screenshots:

<img width="1427" alt="book appointments" src="https://github.com/user-attachments/assets/2ab45bef-6648-4cf7-beb8-575df13845f3">

&nbsp;
&nbsp;

<img width="1425" alt="2" src="https://github.com/user-attachments/assets/94aceeab-7a36-4e29-aed3-d6695ecb9a4f">

&nbsp;
&nbsp;

<img width="1425" alt="3" src="https://github.com/user-attachments/assets/9db6eee7-746c-4f32-82dd-28199cc996ec">

&nbsp;
&nbsp;

<b>Steps to build docker images:</b>

1). Build app-service docker image (java, backend) </br>
Go to app-service dir and run command:
````
mvn clean install -P buildDocker
````

2). Build angular-ui docker image (frontend UI) </br>
Go to angular-ui dir and run command: 
````
npm install
ng build
docker build -t justlife-case-study-angular:latest .
````

&nbsp;
&nbsp;

<b>Run application using docker compose:</b> </br>

1). Run MySql db:
````
docker compose up db-server -d
````

2). Run UI server (angular, UI):
````
docker compose up ui-service -d
````

3). Run App server (java, backend):
````
docker compose up app-server -d
````

Note: App server is dependent on MySql db, so please wait few mins for db-server container to be ready before starting app-server container.
