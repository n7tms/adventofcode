(ns aoc2021
  (:require [clojure.string :as string]))


(def day "02")
(def input-file (str "day" day "-input.txt"))

(def small "forward 5
down 5
forward 8
up 3
down 8
forward 2")

(def large (slurp input-file))


(defn split [regex s]
  (string/split s regex))


(def parsed
  (->> large
       (string/split-lines)
       (map #(split #" " %))
       )
)

(def serialized
  (for [x parsed]
    (cond
      (= (first x) "forward") {:horiz (Integer/parseInt (last x))}
      (= (first x) "down") {:depth  (Integer/parseInt (last x))}
      (= (first x) "up") {:depth (- 0 (Integer/parseInt (last x)))})
    ))  

(defn total-horiz [x]
  (->> x
       (map :horiz)
       (remove nil?)
       (reduce +)
       ))

(defn total-depth [x]
  (->> x
       (map :depth)
       (remove nil?)
       (reduce +)
       ))


(defn part1 []
  (* (total-horiz serialized) (total-depth serialized))
  )

(part1)   ;; => 1635930



(defn part2 []
  (loop [x 0
         aim 0
         horiz 0
         depth 0]
    (if (< x (count parsed))
      (let [cmd (first (nth parsed x))
            mag (Integer/parseInt (last (nth parsed x)))]
        (cond
          (= cmd "forward") (recur (inc x) aim (+ horiz mag) (+ depth (* aim mag)))
          (= cmd "down")    (recur (inc x) (+ aim mag) horiz depth)
          (= cmd "up")      (recur (inc x) (- aim mag) horiz depth)
          )
        )
      (* horiz depth)
      )
    )
  )

(part2)  ;; => 1781819478



;; ===============================================
;;     Playing with alternate forms
;;   (Most of this from Gavin Sinclair)
;; ===============================================

(reduce (fn [acc x] (+ (* acc 2) x)) [6 3 2 8])
(reductions (fn [acc x] (+ (* acc 2) x)) [6 3 2 8])


(def parse-small
  (->> small
       (string/split-lines)
       (map #(split #" " %))
       )
)

(defn part1-again []
  (let [p (fn [s]
            (let [[a b] s]
              {:instr (keyword a) :val (Integer/parseInt b)}))
        acc-0 {:pos 0, :dep 0}
        f (fn [acc instruction]
            (let [{:keys [instr val]} instruction]
              (case instr
                :forward (update acc :pos + val)
                :down    (update acc :dep + val)
                :up      (update acc :dep - val))))
        ]
    
    (->> parsed
         (map p)
         (reduce f acc-0)
         vals
         (apply *)
         )
    ))

(defn parse-02 [s]
  (let [[a b] s]
    {:instr (keyword a) :val (Integer/parseInt b)})  )


(defn part2-again []
  (let [acc-0 {:pos 0, :dep 0, :aim 0}
        f (fn [acc instruction]
            (let [{:keys [instr val]} instruction]
              (case instr
                :forward (-> acc
                             (update :pos + val)
                             (update :dep + (* val (:aim acc))))
                :down    (update acc :aim + val)
                :up      (update acc :aim - val))))
        ]
    
    (->> parsed
         (map parse-02)
         (reduce f acc-0)
         ((juxt :pos :dep))
         (apply *)
         )
    ))

(part2-again)



