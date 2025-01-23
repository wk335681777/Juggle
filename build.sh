cd console-ui;
npm run build;
cd ..;
mvn package -DskipTests=true;
cp console/target/juggle-server.jar /d/app/juggle-1.2.3/target/;