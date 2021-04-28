# VSCode devcontainer

When opening this repo inside VSCode you should automatically be offered to open it inside a devcontainer.
Accepting this will build the docker image and bootstrap a VSCode server inside the container started from that image.
This container has everything required to build tolar-gateway. All you have to do is open a terminal to the container and run:

```sh
mvn clean package
```

If the build is successful a binary will be produced in the **target** folder.

Read more about VSCOde Remote Containers in the [official documentation](https://code.visualstudio.com/docs/remote/containers).