This is a booking application, where you can book staff for cleaning services at one of available time slots.

Application screenshots:

<img width="1427" alt="book appointments" src="https://github.com/user-attachments/assets/2ab45bef-6648-4cf7-beb8-575df13845f3">


<img width="1425" alt="2" src="https://github.com/user-attachments/assets/94aceeab-7a36-4e29-aed3-d6695ecb9a4f">


<img width="1425" alt="3" src="https://github.com/user-attachments/assets/9db6eee7-746c-4f32-82dd-28199cc996ec">




Steps to build docker images:

1). Build app-service docker image (java, backend)
Go to app-service dir and run command:
mvn clean install -P buildDocker

2). Build angular-ui docker image (frontend UI)
Go to angular-ui dir and run command: 
docker build -t justlife-case-study-angular:latest .

(Note: before building docker image for angular, make sure angular project has a build available in dist folder.
(run 'ng build' command to build angular project first before building docker image))



Run application using docker compose:
1). Run MySql db:
docker compose up db-server -d

2). Run UI server (angular, UI):
docker compose up ui-service -d

3). Run App server (java, backend):
docker compose up app-server -d

Note: App server is dependent on MySql db, so please wait few mins for db-server container to be ready before starting app-server container.
