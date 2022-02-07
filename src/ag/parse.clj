(ns ag.parse
  (:require [instaparse.core :as insta])
  (:gen-class))

(def query
  "Find in [worda wordb] from last 25 hours")

(def query2
  "Find in [ worda wordb $topic ] from last 2 hours")

(def query3
  "Define $fra [Macron Castex]")

(def parse
  (insta/parser
   "S = FINDLAST | DEF
    FINDLAST = <FINDIN> <LBKT> SYMORWRDGRP+ <RBKT> <FROMLAST> HOURS <REST>
    LBKT = '['
    RBKT = ']'
    <LWSP> = #'\\s*'
    FINDIN = #'Find in '
    FROMLAST = #' from last '
    WORD = #'[a-zA-Z0-9]+'
    <WORDGRP> = <LWSP> WORD <LWSP>
    SYMBOL = #'\\$[a-zA-Z0-9]+'
    SYMGRP = <LWSP> SYMBOL <LWSP>
    <SYMORWRDGRP> = SYMGRP | WORDGRP
    HOURS = #'\\d+'
    REST = #' hours'
    DEF = <DEFPFX> SYMGRP <LBKT> WORDGRP+ <RBKT>
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
  ()
  (doseq [item (:content (parse query))]
    (println item)))
