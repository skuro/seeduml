# SeedUML

<img src="http://skuro.tk/img/post/seeduml.png" alt="SeedUML" align="right" title="SeedUML" />

> To see things in the seed,
> that is genius
>
> - Lao Tzu

Edit and share your diagrams, online.

## Intro

[SeedUML](http://seeduml.com) allows you to create, edit and share diagrams online. Using [PlantUML](http://plantuml.sourceforge.net) syntax, you write plain text descriptions of your diagrams, which are rendered on the fly. All diagrams get a unique ID and the generated image URL can be used to embed the diagram wherever you need it.

## Disclaimer

The software provided here is in early beta stages, expect any thing to break unexpectedly at any time!

## Usage

SeedUML uses [Neo4j](http://www.neo4j.org/) to store PlantUML diagrams. SeedUML will expect to find a running Neo4j instance ad `http://localhost:7474/db/data`. You can use the following system properties to configure the connection parameters used by SeedUML to attach to your Neo4j instance:

    NEO4J_URL
    NEO4J_LOGIN
    NEO4J_PASSWORD

You can then run it through:

    lein run -m seeduml.web 8080

After some seconds, SeedUML will happily let you edit your diagrams at `http://localhost:8080`.

## License

Copyright © 2013 Carlo Sciolla a.k.a. skuro

Distributed under the Eclipse Public License, the same as Clojure.
