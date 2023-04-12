In order to run the application. You will need to get [an open ai api key](https://beta.openai.com/account/api-keys), and add it to your .profile or .zshrc

`export OPEN_AI_KEY=your_key_goes_here`

You can then use the run configuration Run Server, in IntelliJ Idea to start the application

If you are not using IntelliJ Idea, you can run the application with the following command

`./start_server.sh`



The service currently doesn't have a frontend, so you can use curl to test it out.

`curl -X POST -H "Content-Type: application/json" -d '{"text": "Say this is a test"}' http://0.0.0.0:8000/oracle`