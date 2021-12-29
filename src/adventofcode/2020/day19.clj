;; Advent of Code
;; Day 19 - 
;;

(ns adventofcode
  (:require [clojure.string :as string]))

(def small (slurp "day19-smallinput.txt"))

(def large (slurp "day19-input.txt"))

(defn parse-subrules [^String input]
  (for [subrules (string/split input #"\|")]
    (for [index (re-seq #"\d+" subrules)]
      (Integer/parseInt index))))

(defn parse-rules [^String input]
  (for [[ _ index literal subrules]
        (re-seq #"(\d+): (?:\"(\w)\"|(.*))" input)]
    {:left (Integer/parseInt index)
     :right (if (some? literal)
              (first literal)
              (parse-subrules subrules))}))

(defn parse-messages [^String input]
  (re-seq #"\w+" input))

(defn parse-input [^String input]
  (let [[rules messages] (string/split input #"\n *\n")]
    {:rules (->> rules
                 parse-rules
                 (sort-by :left)
                 (mapv :right))
     :messages (parse-messages messages)}))

(defn part2-rules [rules]
  (assoc rules
         8  '((42)      (42 8))
         11 '((42 31)   (42 11 31))))

:input '("abc" "ade" "xyz") '((1) (1 2)) ['((1) (1 2)) \a \b]
:output '("bc" "c" "de")

(defn consume [messages rule rules]
  (cond
    (char? rule)
    (for [^String message messages
          :when (= rule (first message))]
      (rest message))

    (sequential? rule)
    (for [message messages
          subrules rule
          leftover (reduce
                   (fn [submessages subrule]
                     (consume submessages (rules subrule) rules))
                   (list message)
                   subrules)]
      leftover)))



(defn part1 []
       (let [{:keys [rules messages]}  (parse-input large)]
         (count (for [message messages
                      leftover (consume (list message) (rules 0) rules)
                      :when (.isEmpty leftover)]
                  message))))

(time (part1))  ;; => 224

(defn part2 []
       (let [{:keys [rules messages]}  (parse-input large)]
         (count (for [message messages
                      leftover (consume (list message) (rules 0) (part2-rules rules))
                      :when (.isEmpty leftover)]
                  message))))

(time (part2))  ;; => 436
