(ns ag.parse
  (:require [instaparse.core :as insta])
  (:gen-class))

(def query1
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

(def query6
  "Define $felite [$fra Zemmour];\n")

(def broken1
  "Define fra [yes no];\n")

(def broken2
  "Find in [ worda wordb
   $topic ] from last 2 hours\n") ;; no semi

(def parse
  (insta/parser
   "<S> = FINDLAST | DEF
    FINDLAST = <FINDIN> <LBKT> (SYMBOL | WORD )+ <RBKT>
       <FROMLAST> HOURS <REST> <SEMI>
    LBKT = '['
    RBKT = ']'
    SEMI = ';\n'
    FINDIN = 'Find in '
    FROMLAST = 'from last '
    WORD = #'[a-zA-Z0-9]+'
    SYMBOL = #'\\$[a-zA-Z0-9]+'
    HOURS = #'\\d+'
    REST = ' hours'
    DEF = <DEFPFX> SYMBOL <LBKT> (SYMBOL | WORD)+ <RBKT> <SEMI>
    DEFPFX = 'Define '
    "
   :auto-whitespace :standard
   :output-format :enlive))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (parse args))


(comment
  (parse query1)
  (parse query2)
  (parse query3)
  (parse query4)
  (parse query5)
  (parse query6)
  (parse broken1)
  (parse broken2)
  (def queries [query1 query2 query3 query4 query5 query6])
  (mapv parse queries)
  (get (mapv parse queries) 5)

  (mapv parse [broken1 broken2])

  (doseq [item (:content (parse query))]
    (println item)))
