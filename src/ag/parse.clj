(ns ag.parse
  (:require [instaparse.core :as insta])
  (:gen-class))

(def query
  "Find in [worda wordb] from last 25 hours")

(def query2
  "Find in [ worda wordb $topic ] from last 2 hours")

(def findlast
  (insta/parser
   "FINDLAST = <FINDIN> <LBKT> SYMORWRDGRP+ <RBKT> <FROMLAST> HOURS <REST>
    LBKT = '['
    RBKT = ']'
    <LWSP> = #'\\s*'
    FINDIN = #'Find in '
    FROMLAST = #' from last '
    WORD = #'[a-z]*'
    <WORDGRP> = <LWSP> WORD <LWSP>
    SYMBOL = #'\\$[a-z0-9]*'
    <SYMGRP> = <LWSP> SYMBOL <LWSP>
    <SYMORWRDGRP> = SYMGRP | WORDGRP
    HOURS = #'\\d+'
    REST = #' hours'"
   :output-format :enlive))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (findlast args))


(comment
  (findlast query)
  (findlast query2)
  (doseq [item (:content (findlast query))]
    (println item)))
