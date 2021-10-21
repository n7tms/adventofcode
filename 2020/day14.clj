(ns adventofcode
  (:require [clojure.string :as string]))

(def small "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
mem[8] = 11
mem[7] = 101
mem[8] = 0")

(def large (slurp "day14-input.txt"))

(defn parse-program [serialized]
  (->> serialized
       (re-seq #"(?:mask = ([01X]+))|(?:mem\[(\d+)\] = (\d+))")
       (map (fn [[_  bitmask   address   value]]
              (if (some? bitmask)
                {:or (-> bitmask
                         (string/replace "X" "0")
                         (Long/parseLong 2))
                 :and (-> bitmask
                          (string/replace "X" "1")
                          (Long/parseLong 2))}
                {:address (Long/parseLong address)
                 :value (Long/parseLong value)})))))

(defn execute [program]
  (reduce
   (fn [state {:keys [or and address value] :as instruction}]
     (if (some? or)
       (assoc state :bitmask instruction)
       (let [{{:keys [or and]} :bitmask} state]
         (update state :memory assoc address
                 (-> value (bit-or or) (bit-and and))))))
   {:bitmask {:or 0, :and -1}
    :memory {}}
   program)
  )

(defn part1 []
  (->> large
       parse-program
       execute
       :memory
       vals
       (reduce +)
       ))

(part1)   ;; => 6317049172545

;; Part 2

(def small2 "mask = 000000000000000000000000000000X1001X
mem[42] = 100
mask = 00000000000000000000000000000000X0XX
mem[26] = 1")



(defn parse-program2 [serialized]
  (->> serialized
       (re-seq #"(?:mask = ([01X]+))|(?:mem\[(\d+)\] = (\d+))")
       (map (fn [[_  bitmask   address   value]]
              (if (some? bitmask)
                {:or (-> bitmask
                         (string/replace "X" "0")
                         (Long/parseLong 2))
                 :float (-> bitmask
                          (string/replace "1" "0")
                          (string/replace "X" "1")
                          (Long/parseLong 2))}
                {:address (Long/parseLong address)
                 :value (Long/parseLong value)})))))

(defn power-bitset [^long bitset]
  (if (zero? bitset) '(0)
      (let [highest (Long/highestOneBit bitset)
            without (bit-xor bitset highest)
            subset (power-bitset without)]
        (concat subset
                (map #(bit-xor highest %) subset)))))


(defn fill-memory [{{:keys [or float]} :bitmask :as state} address value]
  (let [address (bit-or address or)]
    (reduce
     (fn [state bitset]
       (update state :memory assoc (bit-xor address bitset) value))
     state
     (power-bitset float)))
  )

(defn execute2 [program]
  (reduce
   (fn [state {:keys [or float address value] :as  instruction}]
     (if (some? or)
       (assoc state :bitmask instruction)
       (fill-memory state address value)))
   {:bitmask {:or 0, :and -1}
    :memory {}}
   program)
  )

(defn part2 []
  (->> large
       parse-program2
       execute2
       :memory
       vals
       (reduce +)
       ))

(part2)  ;; => 3434009980379
