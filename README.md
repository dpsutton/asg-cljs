A clone of [ASG website](https://github.com/stevenmusumeche/asg-website) in clojurescript.

Built with appreciation using [Shadow CLJS](http://shadow-cljs.org/).

## Requirements

- java
- node

## How to run

`npm install` and then
`npx shadow-cljs watch app` and you will have a live-reloading dev server

The app serves at [localhost:8080](http://localhost:8080/)

``` shell
[dan@fedora asg]$ npx shadow-cljs watch app
shadow-cljs - config: /home/dan/projects/clojure/asg/shadow-cljs.edn  cli version: 2.8.10  node: v11.10.0
shadow-cljs - HTTP server available at http://localhost:8080
shadow-cljs - server version: 2.8.10 running at http://localhost:9630
shadow-cljs - nREPL server started on port 46451
shadow-cljs - watching build :app
[:app] Configuring build.
[:app] Compiling ...
[:app] Build completed. (183 files, 1 compiled, 0 warnings, 5.34s)

```

## Known issues

- Something strange with fonts, not sure what

## Docker

soon. Should be easy. I'm not too familiar with it so I just need to figure out how to mount the source files into an image that has node and openjdk (FROM wombat7/openjdk-node-docker) and then make sure that edits don't screw up permissions.
