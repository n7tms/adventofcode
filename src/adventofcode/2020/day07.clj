;; Advent of Code 2020
;; Day 7
;; https://adventofcode.com/2020/day/7

(ns aoc2020.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))


;; Input file
(def input-file "day07-input.txt")

;; Sample Data
(def sample-data 
"light red bags contain 1 bright white bag, 2 muted yellow bags.
dark orange bags contain 3 bright white bags, 4 muted yellow bags.
bright white bags contain 1 shiny gold bag.
muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
dark olive bags contain 3 faded blue bags, 4 dotted black bags.
vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
faded blue bags contain no other bags.
dotted black bags contain no other bags.")

;; this is what the data comes in as:
(def sample-data-short  "light red bags contain 1 bright white bag, 2 muted yellow bags.")

;; this is what we want to massage it into.
;; '{"light red" ([1 "bright white"] [2 "muted yellow"])}
;; a bag of bags and their quantities
;; This parse-bag function accomplishes that.
(defn parse-bag [serialized]
  (let [[[_ _ container] & elements]
        (re-seq #"(?:^|(\d+) )(\w+ \w+) bags?" serialized)]
    {container (map (fn [[_ n color]]
                    [(Integer/parseInt n) color])
                  elements)})
)

;(parse-bag sample-data-short)

(def database
  (with-open [rdr (io/reader input-file)]
    (->> rdr
         line-seq
         (map parse-bag)
         (apply merge)
         
         )))

;database

(defn sub-contains? [container own-color]
  (->> container
       database
       (some
        (fn [[n color]]
          (or  (= color own-color)
               (sub-contains? color own-color)))))
  )

(defn part1 [bag-color]
  (->> database
       (filter
        (fn [[container elements]]
          (sub-contains? container bag-color)))
       count))


(defn part2 [container]
  (->> container
       database
       (reduce
        (fn [temp [n color]]
          (+ temp n
             (* n (part2 color))))
        0)))



(part1 "shiny gold") ;; =>  326
(part2 "shiny gold") ;; => 5635


;; In all honesty, I had a very difficult time wrapping my head around this solution.
;; Fred Overflow does a great job explaining what he is doing, but I'm still struggling.
;; Watch this again: https://www.youtube.com/watch?v=oFLZqXMeIpw&list=PLbPrugU2oQ8VURsQdZ6W_iovXRS24UmZQ&index=7
;;


