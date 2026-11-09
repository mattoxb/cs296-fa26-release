# interaction

A tiny natural-language REPL that remembers variables and facts about objects.
It is the starting point for the adventure project.

## Your job

Open `src/interaction/core.clj`. Some functions are done for you; fill in the
ones with empty bodies:

- `set-var`
- `describe-obj`
- `forget-obj`
- `canonicalize`
- the missing phrases in `react`

## Running it

```
clojure -M:run
```

That calls `-main` in `interaction.core`, which starts the REPL. Type `bye` to quit.
(`bye` only works once `canonicalize` is written. Until then, `Ctrl+D` exits.)

```
Welcome!  Let's talk.
> there is a spoon.
There is a spoon.
> the spoon is blue.
The spoon is blue.
> describe the spoon.
The spoon is blue.
> bye
```

## Running the tests

```
clojure -M:test
```

Or from Calva: `Ctrl+Alt+C Ctrl+Alt+T` runs the tests for the current namespace.

## Working at a REPL

In VS Code with Calva, jack in (`Ctrl+Alt+C Ctrl+Alt+J`, project type
**deps.edn**, no aliases) and evaluate forms with `Alt+Enter`.

From a terminal:

```clojure
$ clojure -M -r
user=> (require 'interaction.core :reload)
user=> (in-ns 'interaction.core)
interaction.core=> (post-increment {:vars {:x 10}} :x)
[{:vars {:x 11}} 10]
```

Remember `:reload` after you save an edit, or you will keep running the old code.

## Layout

```
deps.edn                          dependencies; :run and :test aliases
src/interaction/core.clj          your code
test/interaction/core_test.clj    the tests
```

## License

Copyright © 2019–2026 Mattox Beckman

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.
