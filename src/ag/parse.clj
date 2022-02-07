(ns ag.parse
  (:require [instaparse.core :as insta])
  (:gen-class))

(def query
  "Find in [worda wordb] from last 25 hours;\n")

(def query2
  "Find in [ worda wordb $topic ] from last 2 hours;\n")

(def query3
  "Define $fra [Macron Castex];\n")

(def query4
  "Find in [ worda wordb
   $topic ] from last 2 hours;\n")

(def broken1
  "Define fra [yes no];\n")

(def broken2
  "Find in [ worda wordb
   $topic ] from last 2 hours\n") ;; no semi

(def parse
  (insta/parser
   "S = (FINDLAST | DEF)
    FINDLAST = <LWSP> <FINDIN> <LBKT> SYMORWRDGRP+ <RBKT> <FROMLAST> HOURS <REST> <SEMI>
    LBKT = '['
    RBKT = ']'
    SEMI = ';' <LWSP>
    <LWSP> = #'\\s*'
    FINDIN = #'Find in '
    FROMLAST = #' from last '
    WORD = #'[a-zA-Z0-9]+'
    <WORDGRP> = <LWSP> WORD <LWSP>
    SYMBOL = #'\\$[a-zA-Z0-9]+'
    SYMGRP = <LWSP> SYMBOL <LWSP>
    <SYMORWRDGRP> = SYMGRP | WORDGRP
    HOURS = #'\\d+'
    REST = #' hours' <LWSP>
    DEF = <LWSP> <DEFPFX> SYMGRP <LBKT> WORDGRP+ <RBKT> <LWSP> <SEMI>
    DEFPFX = #'Define '
    "
   :output-format :enlive))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (parse args))


(comment
  (parse query)
  (parse query2)
  (parse query3)
  (parse query4)
  (parse broken1)
  (parse broken2)

  (parse (str query query3))

  (parse (str query query3 query4))

  (parse (str query broken1 query3 query4))


  (doseq [item (:content (parse query))]
    (println item)))
