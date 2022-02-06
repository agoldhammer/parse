(ns ag.parse
  (:require [instaparse.core :as insta])
  (:gen-class))

(def query
  "Find in [worda wordb] from last n hours")

(def findlast
  (insta/parser
   "S = LAST LBKT WORD+ RBKT REST
    LBKT = '['
    RBKT = ']'
    LAST = #'Find in '
    WORD = #'[a-z]*\\s*'
    REST = #'.*'"))



(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (findlast args))


(comment
  (findlast query))
