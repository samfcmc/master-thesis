module.exports = function(grunt) {

  require('load-grunt-tasks')(grunt);

  // Project configuration.
  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    srcPath: 'src',
    js: 'js',
    views: 'views',
    server: 'server.js',
    serverPath: '<%= server %>',
    srcJSPath: '<%= srcPath %>/<%= js %>',
    mainScript: 'main.js',
    vendorsScript: 'vendors.js',
    srcMainPath: '<%= srcJSPath %>/<%= mainScript %>',
    srcVendorsPath: '<%= srcJSPath %>/<%= vendorsScript %>',
    srcViewsPath: '<%= srcPath %>/<%= views %>',
    buildPath: 'build',
    buildJSPath: '<%= buildPath %>/<%= js %>',
    buildMainPath: '<%= buildJSPath %>/<%= mainScript %>',
    buildVendorsPath: '<%= buildJSPath %>/<%= vendorsScript %>',
    buildViewsPath: '<%= buildPath %>/<%= views %>',
    bowerComponents: 'bower_components',
    bowerTargetPath: '<%= buildPath %>/<%= bowerComponents %>',
    debug: '<%= grunt.config.get("debug") %>',
    config: {
      dev: {
        options: {
          variables: {
            debug: true
          }
        }
      },
      prod: {
        options: {
          variables: {
            debug: false
          }
        }
      }
    },
    bower: {
      install: {
         options: {
           targetDir: '<%= bowerTargetPath %>'
         }
      }
    },
    browserify: {
      main: {
        files: {
          '<%= buildMainPath %>': ['<%= srcMainPath %>'],
        },
        options: {
          browserifyOptions: {
            debug: '<%= debug %>'
          }
        }
      },
      vendors: {
        files: {
          '<%= buildVendorsPath %>': ['<%= srcVendorsPath %>']
        },
        options: {
          transform: ['debowerify']
        }
      }
    },
    copy: {
      views: {
        files: [
          // includes files within path
          {
            expand: true,
            cwd: '<%= srcViewsPath %>',
            src: ['**/*.html'],
            dest: '<%= buildViewsPath %>'
          },
        ],
      },
    },
    watch: {
      bower: {
        files: ['bower.json'],
        tasks: ['bower:install']
      },
      main: {
        files: ['<%= srcJSPath %>/**/*.js'],
        tasks: ['config:dev', 'browserify:main']
      },
      vendors: {
        files: ['<%= srcVendorsPath %>'],
        tasks: ['config:dev', 'browserify:vendors']
      },
      views: {
        files: ['<%= srcViewsPath %>/**/*.html'],
        tasks: ['config:dev', 'copy:views']
      }
    },
    clean: {
      build: ['<%= buildPath %>'],
    },
    express: {
      server: {
        options: {
          port: 9000,
          server: '<%= serverPath %>'
        }
      }
    },
    uglify: {
      prod: {
        files: {
          '<%= buildMainPath %>': ['<%= buildMainPath %>'],
          '<%= buildVendorsPath %>': ['<%= buildVendorsPath %>']
        }
      }
    }
  });

  grunt.registerTask('common', ['bower', 'browserify', 'copy']);
  grunt.registerTask('dev', ['config:dev', 'common', 'express', 'watch']);
  grunt.registerTask('prod', ['config:prod', 'common']);
  grunt.registerTask('default', ['dev']);

};
