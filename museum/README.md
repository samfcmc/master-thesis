# SLOC-API-and-RestApp
A simple web application to order food in a restaurant
Also, it offers an interface to manage the orders.

## Requirements
### Using Vagrant
* [Virtualbox](https://www.virtualbox.org/)
* [Vagrant](https://www.vagrantup.com/)

### Using Grunt
* [NodeJS](https://nodejs.org/)
* [NPM](https://www.npmjs.com/)
* [Grunt-cli](http://gruntjs.com/)

After installing nodejs and npm you can install grunt with:
```
npm install -g grunt-cli
```
## Run in your local machine:
### Using vagrant
In the project's root directory (the same directory where is the ```Vagrantfile```) run:
```
vagrant up
```

To stop it:
```
vagrant halt
```
### Using grunt
Just run:
```
grunt
```
This command will open a web browser window in the address where your web application is running.


After you start the local web server using any of the methods above, you can access it in:
http://localhost:9000

The user's app is located at:
http://localhost:9000/restApp/restapp.html

The manager's app is located at:
http://localhost:9000/restApp/restmanager.html
