# Project Euler in Clojure

Starter project for live-coding Project Euler solutions.

## One-time setup

1. Install a JDK (21 works; anything 11+ is fine).
2. Install the [Clojure CLI](https://clojure.org/guides/install_clojure) — provides the `clojure` command.
3. In VS Code, install the **Calva** extension. Opening this folder will prompt you,
   since it is listed in `.vscode/extensions.json`.

Check your install from a terminal:

```
clojure -M -e '(println "ready")'
```

## Starting a REPL ("jack-in")

1. Open **this folder** in VS Code (`File > Open Folder...`), not a parent directory.
   Calva looks for `deps.edn` at the root of the open folder.
2. Press `Ctrl+Alt+C` then `Ctrl+Alt+J` (Calva: Start a Project REPL and Connect).
3. When asked for a project type, choose **deps.edn**.
4. When asked which aliases to launch with, pick none and press Enter.

A REPL window opens and the status bar shows the connection.

## Evaluating code

Open `src/euler/core.clj`. Put the cursor anywhere inside a top-level form and
press `Alt+Enter` to evaluate it; the result appears inline.

Evaluation only works when the editor has focus and the file is recognized as
Clojure — the bottom-right of the status bar should read `Clojure`. If nothing
happens, click into the code first; keypresses sent to the REPL output pane or
a terminal will not evaluate.

The `(comment ...)` block at the bottom of `core.clj` is a scratch area — code in
it never runs on load, but you can evaluate the forms inside one at a time. This
is the normal Clojure workflow.

## Using the REPL from a terminal

You do not need VS Code. From this folder:

```
clojure -M -r
```

That starts a REPL in the `user` namespace, which does **not** have your code in
it yet. Load it and switch in:

```clojure
(require 'euler.core)
(in-ns 'euler.core)
(plus-and-one 2 3)
;; => 6
```

The prompt changes from `user=>` to `euler.core=>` once `in-ns` succeeds, and
from there you can call functions by their short names.

If you skip the `require` and jump straight to calling something, you get a
confusing error rather than "no such function":

```
user=> (euler.core/plus-and-one 1 1)
Syntax error (ClassNotFoundException) compiling at (REPL:1:1).
euler.core
```

That message means "namespace never loaded" — run the `require` first.

### Picking up your edits

A REPL does not re-read files on its own. After saving a change to
`src/euler/core.clj`, reload it in the running REPL:

```clojure
(require 'euler.core :reload)
```

Without `:reload`, a second `require` is a no-op and you keep calling the old
definition. Exit the REPL with `Ctrl+D`.

### Running something once, without a REPL

To evaluate an expression and quit — handy for checking an answer:

```
clojure -M -e "(require 'euler.core) (println (euler.core/plus-and-one 40 1))"
```

Note the fully-qualified `euler.core/plus-and-one` here: there is no `in-ns`, so
the short name is not in scope.

## Running the tests

From a terminal:

```
clojure -M:test
```

Or from Calva: `Ctrl+Alt+C Ctrl+Alt+T` runs the tests for the current namespace.

## Layout

```
deps.edn              dependencies and the :test alias
src/euler/core.clj    solution code
test/euler/core_test.clj
```

Namespace names map to file paths: `euler.core` lives at `src/euler/core.clj`.
Hyphens in a namespace become underscores in the filename, which is why the test
file is `core_test.clj` and not `core-test.clj`.
