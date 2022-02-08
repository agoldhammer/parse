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

(def query5
  "Find in [ worda wordb
   $topic ] from last 2 hours ;\n")

(def broken1
  "Define fra [yes no];\n")

(def broken2
  "Find in [ worda wordb
   $topic ] from last 2 hours;\n") ;; no semi

(def parse
  (insta/parser
   "S = (FINDLAST | DEF)
    FINDLAST = <FINDIN> <LBKT> (<LWSP> SYMBOL <LWSP> | <LWSP> WORD <LWSP>)+ <RBKT>
      <LWSP> <FROMLAST> HOURS <REST> <LWSP> <SEMI>
    LBKT = '['
    RBKT = ']'
    SEMI = ';\n'
    <LWSP> = #'\\s*'
    FINDIN = 'Find in '
    FROMLAST = 'from last '
    WORD = #'[a-zA-Z0-9]+'
    SYMBOL = #'\\$[a-zA-Z0-9]+'
    HOURS = #'\\d+'
    REST = ' hours'
    DEF = <DEFPFX> SYMBOL <LWSP> <LBKT> (<LWSP> WORD <LWSP>)+ <RBKT> <LWSP> <SEMI>
    DEFPFX = 'Define '
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
  (parse query5)
  (parse broken1)
  (parse broken2)
  (subs query3 11)



  (doseq [item (:content (parse query))]
    (println item)))
