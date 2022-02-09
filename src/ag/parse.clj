(ns ag.parse
  (:require [instaparse.core :as insta])
  (:gen-class))

(def query1
  "Find in [worda wordb] from last 25 hours;")

(def query2
  "Find in [ worda wordb $topic ] from last 2 hours;")

(def query3
  "Define $fra [Macron Castex];")

(def query4
  "Find in [ worda wordb
   $fra ] from last 2 hours;")

(def query5
  "Find in [ worda wordb
   $topic ] from last 2 hours ;")

(def query6
  "Define $felite [$fra Zemmour];")

(def broken1
  "Define fra [yes no];")

(def broken2
  "Find in [ worda wordb
   $topic ] from last 2 hours") ;; no semi

(def parse
  (insta/parser
   "<S> = FINDLAST | DEF
    FINDLAST = <FINDIN> <LBKT> (SYMBOL | WORD )+ <RBKT>
       <FROMLAST> HOURS <REST> <SEMI>
    LBKT = '['
    RBKT = ']'
    SEMI = ';'
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

(def symbol-table (atom {}))

(defn reset-symbol-table!
  "reset the symbol table to empty map"
  []
  (reset! symbol-table {}))

(defn add-symbol!
  "add symbol to symbol table"
  [symbol vec-of-words]
  (swap! symbol-table assoc symbol vec-of-words))

(defn build-findlast
  "reducing fn to build a findlast command"
  [acc node]
  (let [tag (:tag node)
        content (first (:content node))]
    (condp = tag
      :WORD (update-in acc [:words] conj content)
      :SYMBOL (update-in acc [:words] into (get @symbol-table content))
      :HOURS (assoc-in acc [:time] content))))

;; FINDLAST content looks like this
;; ({:tag :WORD, :content ("worda")}
;;   {:tag :WORD, :content ("wordb")}
;;   {:tag :SYMBOL, :content ("$topic")}
;;   {:tag :HOURS, :content ("2")})
(defn analyze-findlast
  "analyze the node of type FINDLAST"
  [content]
  (println "analyze-findlast" content)
  (let [command {:words [] :time ""}]
    (reduce build-findlast command content)))

;; DEF content looks like
;; ({:tag :SYMBOL, :content ("$fra")}
;;  {:tag :WORD, :content ("Macron")}
;;  {:tag :WORD, :content ("Castex")})
(defn analyze-def
  "analyze node of type DEF"
  [content]
  (let [symbol-node (first content)
        symbol (first (:content symbol-node))
        tag (:tag symbol-node)
        vec-of-words (mapv (comp first :content) (rest content))]
    (when (not= tag :SYMBOL) ;; sanity check
      (println "Error in symbol node" symbol-node)
      (throw (Exception. "Symbol error")))
    (add-symbol! symbol vec-of-words)))

;; parser output looks like
;; ({:tag :FINDLAST
;;  :content
;;  ({:tag :WORD, :content ("worda")}
;;   {:tag :WORD, :content ("wordb")}
;;   {:tag :SYMBOL, :content ("$topic")}
;;   {:tag :HOURS, :content ("2")})})

(defn analyze
  "analyze parser output"
  [parsed]
  (let [node (first parsed)
        content (:content node)]
    (condp = (:tag node)
      :FINDLAST (analyze-findlast content)
      :DEF (analyze-def content)
      "No matching node type")))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (parse args))


(comment
  (parse query1)
  (analyze (parse query1))
  (parse query2)
  (analyze (parse query2))
  (parse query3)
  (analyze (parse query3))
  (println @symbol-table)
  (reset-symbol-table!)
  (parse query4)
  (analyze (parse query4))
  (parse query5)
  (parse query6)
  (parse broken1)
  (parse broken2)
  (def queries [query1 query2 query3 query4 query5 query6])
  (mapv parse queries)
  (get (mapv parse queries) 5)

  (mapv parse [broken1 broken2])

  (doseq [item (:content (parse query1))]
    (println item)))
