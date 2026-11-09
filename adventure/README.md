# adventure

Starter project for the text adventure. See `adventure.md` for the requirements.

The code in `src/adventure/core.clj` already runs: you can walk between two rooms
and quit. Replace the world with your own and add the verbs you need.

## Running it

```
clojure -M:run
```

This calls `-main` in `adventure.core`. This is the command used for your demo,
so make sure it works.

## Running the tests

```
clojure -M:test
```

## Working at a REPL

In VS Code with Calva, jack in (`Ctrl+Alt+C Ctrl+Alt+J`, project type
**deps.edn**, no aliases) and evaluate forms with `Alt+Enter`.

From a terminal:

```clojure
$ clojure -M -r
user=> (require 'adventure.core :reload)
user=> (in-ns 'adventure.core)
adventure.core=> (go init-state :south)
```

## Layout

```
deps.edn                         dependencies; :run and :test aliases
src/adventure/core.clj           your game
test/adventure/core_test.clj     tests
```
