# Squall

[![build](https://github.com/openclosed-dev/squall/actions/workflows/build.yml/badge.svg)](https://github.com/openclosed-dev/squall/actions/workflows/build.yml)

Squall is a suite of tools and libraries for generating database design documents from DDL/SQL source code annotated with Javadoc/JSDoc-style comments.

## Features

* Parses DDL sources directly without running database server.
* Generates documents describing database design in various kinds of formats: HTML, PDF, AsciiDoc, Markdown, and JSON.
* Recognizes Javadoc/JSDoc-style doc comments including special tags for annotating schema objects.
* Provides both CLI tool and API for JVM languages.

Note that the only SQL dialect currently supported is PostgreSQL.

## Doc Comments

A doc comment is a multi-line comment that starts with `/**` and ends with `*/`.
This type of comment can be used to annotate objects such as database, schema, sequence, table, and table column,  immediately before CREATE statement for these objects begins.
The example below shows how the doc comments are applied to tables and columns in a database schema.

```sql
/**
 * Baseball teams in MLB.
 * @label Baseball team
 */
CREATE TABLE baseball_team(
  /**
   * Unique ID of the team.
   * @label ID
   */
  id integer PRIMARY KEY,
  /**
   * The name of the team.
   * @label Name
   */
  name varchar(128) NOT NULL,
  /**
   * The league to which this team belongs.
   * @label Affiliated league
   */
  affiliated_league char(2) NOT NULL REFERENCES league(id),
  /**
   * The year when this team was established.
   * @label Year established
   */
  year_established integer NOT NULL
);
```

The description part in doc comments, that precedes any tag is treated as content of Markdown, or more specifically of [CommonMark](https://commonmark.org/).

Doc comments are allowed to contain one or more tags for annotating target schema objects.

| Tag | Description |
| --- | --- |
| `@label` | Defines the logical name of the object for display purpose. |
| `@since` | Specifies the version when the object was added first. |
| `@deprecated` | Warns the object is deprecated and will be removed soon. |

## Installation

[Releases Page](https://github.com/openclosed-dev/squall/releases) of this project provides the following types of distributions for each release.

* MSI format for Windows
* ZIP format for Windows

Every distribution file includes 64-bit Java runtime. There are no plans to support 32-bit platforms.

After the installation was completed, your console window need to be closed and reopened for reloading the PATH environment variable modified during the installation process.

If the zip format is convenient, all you have to do is to unpack the archive wherever you prefer and add the directory to the PATH environment variable.

The successful installation can be confirmed by invoking the following command.
```
squall -v
```

## Using the CLI

The installed or unpacked package of any supported platform contains an executable file named `squall`.

The command provides subcommands shown below.

| Subcommand | Description |
| --- | --- |
| config | Manage configurations. |
| spec | Manage database design specification. |

### config subcommand

The following invocation of the command generates an initial configuration file named `squall.json` in the current directory.

```
squall config init
```

First of all, your DDL source files must be added in the `sources` property found in the JSON file.

```json
{
  "sources": [
    "your-ddl-file.sql",
    "another-ddl-file.sql"
  ],
  ...
}
```

The path can be absolute, or relative to the configuration file.

The order of the entries is relevant. The parser processes the source files one by one exactly in the same order as that of the entries in the configuration. A source file needs to be parsed after all its dependency files are processed.

### spec subcommand

Database design documents can be generated from parsed DDL sources by invoking `spec render` command as follows.

```
squall spec render <name of renderer>
```

A set of renderers are predefined in the configuration file. If the renderer of your choice is named `default` in the configuration, the name can be ommited on the command line like this:

```
squall spec render
```

Multiple renderers can be specified at the same line. In this case, specified renderers will be executed one by one.

```
squall spec render default pdf markdown
```

## Future Plans

* More rich set of tags for producing testing data. e.g. `@format email`
* Automatic generation of ER diagrams
* Verification of database designs, as the Checkstyle is doing to program codes.
* Support of more SQL dialects.

## Copyright Notice

Copyright 2022-2023 The Squall Authors. All rights reserved.

This software is licensed under [Apache License, Version 2.0][Apache 2.0 License].

[Apache 2.0 License]: https://www.apache.org/licenses/LICENSE-2.0
