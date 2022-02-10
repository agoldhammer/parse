(ns parse-test
  (:require [clojure.test :refer [deftest is testing]]
            [ag.parse :as p]))

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

(def queries [query1 query2 query3 query4 query5 query6])
(def expected-parse-results (quote [({:tag :FINDLAST
                                      :content
                                      ({:tag :WORD, :content ("worda")}
                                       {:tag :WORD, :content ("wordb")}
                                       {:tag :HOURS, :content ("25")})})
                                    ({:tag :FINDLAST
                                      :content
                                      ({:tag :WORD, :content ("worda")}
                                       {:tag :WORD, :content ("wordb")}
                                       {:tag :SYMBOL, :content ("$topic")}
                                       {:tag :HOURS, :content ("2")})})
                                    ({:tag :DEF
                                      :content
                                      ({:tag :SYMBOL, :content ("$fra")}
                                       {:tag :WORD, :content ("Macron")}
                                       {:tag :WORD, :content ("Castex")})})
                                    ({:tag :FINDLAST
                                      :content
                                      ({:tag :WORD, :content ("worda")}
                                       {:tag :WORD, :content ("wordb")}
                                       {:tag :SYMBOL, :content ("$fra")}
                                       {:tag :HOURS, :content ("2")})})
                                    ({:tag :FINDLAST
                                      :content
                                      ({:tag :WORD, :content ("worda")}
                                       {:tag :WORD, :content ("wordb")}
                                       {:tag :SYMBOL, :content ("$topic")}
                                       {:tag :HOURS, :content ("2")})})
                                    ({:tag :DEF
                                      :content
                                      ({:tag :SYMBOL, :content ("$felite")}
                                       {:tag :SYMBOL, :content ("$fra")}
                                       {:tag :WORD, :content ("Zemmour")})})]))
(deftest query-test
  (testing "findlast queries"
    (doseq [i (range (count queries))]
      (is (= (p/parse (nth queries i)) (nth expected-parse-results i))))))

(comment
  (p/parse query6)
  (def a '({:tag FINDLAST :content ("words")}))
  a
  (:tag (first a))
  expected-parse-results
  (count expected-parse-results)
  (nth expected-parse-results 4)
  (= (p/parse query5) (nth expected-parse-results 4))
  (count queries)
  (nth queries 2)
  (first queries)
  #_(query-t queries expected-results)
  (p/parse query1)
  (p/analyze (p/parse query1))
  (p/parse query2)
  (p/analyze (p/parse query2))
  (p/parse query3)
  (p/analyze (p/parse query3))
  (println @p/symbol-table)
  (p/reset-symbol-table!)
  (p/parse query4)
  (p/analyze (p/parse query4))
  (get @p/symbol-table "$nonexistent")
  @p/symbol-table
  (p/analyze (p/parse "Find in [ $nonexistent ] from last 2 hours;"))
  (p/analyze (p/parse "Define $topic [Germany France];"))
  (p/parse query5)
  (p/parse query6)
  (p/analyze (p/parse "Find in [$fra $topic] from last 12 hours;"))
  (p/parse broken1)
  (p/analyze (p/parse broken1))
  (p/analyze (p/parse broken2))
  (def queries [query1 query2 query3 query4 query5 query6])
  (mapv p/parse queries)
  (get (mapv p/parse queries) 5)

  (mapv p/parse [broken1 broken2])

  (doseq [item (:content (p/parse query1))]
    (println item)))