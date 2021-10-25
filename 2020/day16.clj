;; Advent of Code
;; Day 16 - Ticket Translation
;;
;; Note: I am embarassed by the fact that I need to continue to plagarize Fred Overflow's code,
;; but -- although I can conceive ideas for a solution -- I do not have the wherewithall to 
;; implement them in clojure ... yet.
;; (Thank you, Fred, for your help!)
;;

(ns adventofcode
  (:require [clojure.string :as str]))

(defn split [regex s]
  (str/split s regex))

(def small "class: 1-3 or 5-7
row: 6-11 or 33-44
seat: 13-40 or 45-50

your ticket:
7,1,14

nearby tickets:
7,3,47
40,4,50
55,2,20
38,6,12")

(def large (slurp "day16-input.txt"))

(defn parse-rules [rules]
  (for [[ _ field min1 max1 min2 max2]
        (re-seq #"(\w[ \w]+): (\d+)-(\d+) or (\d+)-(\d+)" rules)]
    [field
     (Integer/parseInt min1)
     (Integer/parseInt max1)
     (Integer/parseInt min2)
     (Integer/parseInt max2)]
    ))


(defn parse-ticket [ticket]
  (->> ticket
       (re-seq #"\d+")
       (mapv #(Integer/parseInt %))))

(defn parse [input]
  (let [[rules ticket nearbys] (string/split input #"\n *\n[^:]+:\n")]
    {:rules (parse-rules rules)
     :ticket (parse-ticket ticket)
     :nearbys (map parse-ticket (string/split-lines nearbys))}))

(:nearbys (parse small))
(:rules (parse small))

(defn satisfies-rule [number [_ min1 max1 min2 max2]]
  (or
   (<= min1 number max1)
   (<= min2 number max2)))

(defn invalid-number [number rules]
  (if-not (some #(satisfies-rule number %) rules)
    number))

(defn some-invalid-number [ticket rules]
  (some #(invalid-number % rules) ticket))

(defn part1 [input]
  (let [{:keys [rules ticket nearbys]} (parse input)]
    (->> nearbys
         (map #(some-invalid-number % rules))
         (remove nil?)
         (reduce +))))

(part1 large)


(defn applicable-rules [column rules]
  (reduce
   (fn [rules number]
     (filter #(satisfies-rule number %) rules))
   rules
   column))

(defn assoc-unique [m [keys value]]
  (let [[unique] (remove m keys)]
    (assoc m unique value)))


(apply map vector [[1 2 3] [4 5 6]])   ;; => ([1 4] [2 5] [3 6]) (rotates the matrix)


(defn part2 [input]
  (let [{:keys [rules ticket nearbys]} (parse input)]
    (->> nearbys
         (remove #(some-invalid-number % rules))
         (apply map vector)
         (map-indexed (fn [index number]
                        [(applicable-rules number rules) index]))
         (sort-by (fn [[rules index]] (count rules)))
         (reduce assoc-unique {})
         (filter (fn [[[name] index]]
                   (string/starts-with? name "departure")))
         (map (fn [[rule index]] (ticket index)))
         (reduce *)

         )))

(part2 large)   ;; => 4381476149273
