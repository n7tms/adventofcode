(ns aoc.day-22
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn parse-input [input-str]
  (->> (str/split input-str #"\R\R")
       (mapv (fn [deck]
               (let [[_ & cards] (str/split-lines deck)]
                 (mapv #(Integer/parseInt %) cards))))))



;; Demo input
(def input-sample (parse-input "Player 1:
9
2
6
3
1

Player 2:
5
8
4
7
10"))


; Real input
(def input
  (parse-input "Player 1:
48
23
9
34
37
36
40
26
49
7
12
20
6
45
14
42
18
31
39
47
44
15
43
10
35

Player 2:
13
19
21
32
27
16
11
29
41
46
33
1
30
22
38
5
17
4
50
2
3
28
8
25
24"))



;; Part 1
(defn play-game [deck1 deck2]
  (cond (empty? deck1) deck2
        (empty? deck2) deck1
        :else
        (let [c1 (first deck1)
              c2 (first deck2)]
          (if (< c1 c2)
            (recur (subvec deck1 1)
                   (conj (subvec deck2 1) c2 c1))
            (recur (conj (subvec deck1 1) c1 c2)
                   (subvec deck2 1))))))

(defn score [deck]
  (apply + (map * deck (range (count deck) 0 -1))))

(score (play-game (first input) (second input)))


;; Part 2
(defn play-rec-game [seen-decks deck1 deck2]
  (cond (seen-decks [deck1 deck2]) [1 deck1]
        (empty? deck1) [2 deck2]
        (empty? deck2) [1 deck1]
        :else
        (let [c1 (first deck1)
              c2 (first deck2)
              seen-decks (conj seen-decks [deck1 deck2])
              player1-win? (if (and (> (count deck1) c1)
                                    (> (count deck2) c2))
                             (= 1 (first (play-rec-game #{}
                                                        (subvec deck1 1 (+ 1 c1))
                                                        (subvec deck2 1 (+ 1 c2)))))
                             (> c1 c2))]
          (if player1-win?
            (recur seen-decks
                   (conj (subvec deck1 1) c1 c2)
                   (subvec deck2 1))
            (recur seen-decks
                   (subvec deck1 1)
                   (conj (subvec deck2 1) c2 c1))))))

(score (second (play-rec-game #{}
                              (first input)
                              (second input))))
