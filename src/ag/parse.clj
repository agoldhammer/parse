(ns ag.parse
  (:require [instaparse.core :as insta])
  (:gen-class))

(def query
  "Find in [worda wordb] from last 5 hours")

(def findlast
  (insta/parser
   "S = <FINDIN> <LBKT> WORDGRP+ <RBKT> <FROMLAST> HOURS <REST>
    LBKT = '['
    RBKT = ']'
    LWSP = #'\\s*'
    FINDIN = #'Find in '
    FROMLAST = #' from last '
    WORD = #'[a-z]*'
    WORDGRP = WORD LWSP
    HOURS = #'\\d+'
    REST = #' hours'"
   :output-format :enlive))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (findlast args))


(comment
  (findlast query)
  (doseq [item (:content (findlast query))]
    (println item)))
