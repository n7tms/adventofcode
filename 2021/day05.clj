(ns aoc2021
  (:require [clojure.string :as string]))

(def day "05")
(def input-file (str "day" day "-input.txt"))


(def small "")

(def large (slurp input-file))

;; globally switch between the small and large dataset.
;(def active small)
(def active large)



(defn split [regex s]
  (string/split s regex))





(defn part1 []

  )




(defn part2 []

  )





