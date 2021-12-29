(ns aoc2021
  (:require [clojure.string :as string]
            [clojure.test :as test]))


;; TODO: change 00 to current day number
(def day "19")
(def input-file (str "day" day "-input.txt"))

(def small (slurp "day19-input-small.txt"))

(def large (slurp input-file))

(def active small)
;(def active large)

(defn split [regex s]
  (string/split s regex))

(def scanners 
(->> active
     (split #"\n\n")))

(count scanner)
(defn parse-beacons [beacon]
  (let [[_ x y z]
        (re-matches #"(\d+),(\d+),(\d+)" beacon)]
    [x y z]))

(defn beacons [scanner-data]
  (let [data (string/split-lines scanner-data)
        scanner-name (last (re-matches #"--- scanner (\d+) ---" (first data)))
        beacs (->> (rest scanner-data)
                   (map #(parse-beacons %)))]
    [(keyword scanner-name) beacs ]))

(beacons (first scanners))


(defn part1 []

  )




(defn part2 []

  )



(println "Part1: " (part1) "\nPart2: " (part2))


(use 'clojure.test)
(deftest sample-data-test
  (test/is (= (part1) 00))
  (test/is (= (part2) 00)))

(run-tests)

