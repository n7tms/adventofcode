(ns advent-of-code-2021.day11
  (:require  [clojure.java.io :as io]
             [clojure.string :as str]))


;; Lets think of the input as the rectangular grid of a cellular automaton.
;; The grid is stored as a one dimensional vector of cells together with the size of one side.
(def grid (let [cells  (->> (slurp "day11-input.txt")
                               str/split-lines
                               (into [] (mapcat (fn [row]  (map  #(- (int %) 48) row)))))
                   size  (Math/round (Math/sqrt (count cells)))]
               {:cells cells
                :size size}))


;; Individual cells in the grid are adressed by their index. 
;; To calculate the neighbour positions we need the grid size in addition to the cell position.
(defn neighbour-indices
  [cell-index grid-size]                             
  (let [x (mod cell-index grid-size)
        y (quot cell-index grid-size)]
    (->> 
      (map (fn [x-offs y-offs]                                   
             [(+ x x-offs) (+ y y-offs)])
           [1 1 0 -1 -1 -1 0 1] 
           [0 1 1 1 0 -1 -1 -1])
      (filter  (fn [[x y]] (and  (>= x 0) (>= y 0) (< x grid-size) (< y grid-size))))
      (map (fn [[x y]] (+ (* y grid-size) x))))))
    


;; Take until first occurence of first repeating element (including this element).
(defn take-until-first-occurrence-of-first-repeating
  [s]
  (let [first-repeating (->> s
                            (reduce (fn [visited e]
                                      (if (visited e)
                                        (reduced e)
                                        (conj visited e)))
                                    #{}))]    
    (concat (take-while #(not= first-repeating %) s) 
            (list first-repeating))))


;; Given a initial grid and a grid transform (see below) calculate
;; the sequence of transformed grids until a grid repeats.
(defn repeatedly-until-first-cycle 
  [grid grid-transform]
  (take-until-first-occurrence-of-first-repeating (iterate grid-transform grid))) 


;; Grid transfroms
;;
;; A grid transform is a function that transforms a grid into another grid.

;; Grid transform: Apply a cell rule (rule-fn) to each cell.
;; 
;; A cell rule is a function that takes the current cell value and the 
;; current cell values of its neighbour cells and returns the new cell value.
(defn rule-application 
  [rule-fn]
  (fn [grid] 
    (let [grid-size (:size grid)
          cells (:cells grid)]
      (->> cells
           count
           range
           (mapv (fn [cell-index]
                   (let [cell (cells cell-index)
                         neighbour-cells (map cells (neighbour-indices cell-index grid-size))]
                     (rule-fn cell neighbour-cells))))
           (assoc grid :cells)))))

;; Grid transform: Apply another grid transform until a cycle occurs.
(defn repeated-until-first-cycle 
  [grid-transform]
  (fn [grid] 
    #_(last (take-until-first-occurrence-of-first-repeating (iterate grid-transform grid)))
    (last (repeatedly-until-first-cycle grid grid-transform))))

;; Specific grid transforms for our problem:

;; Grid transform: increase the energy level of each cell by 1
(def increase-energy-level 
  (rule-application (fn [cell _] (inc cell))))

;; Grid transform: mark all currently flashing calls (use -1 as marker).
(def mark-flashing 
  (rule-application (fn [cell _]  (if (> cell 9) -1 cell))))


;; Grid transform: increase the energy level by adding 1 for each flashing neighbour
(def increase-energy-level-from-flashing-neighbours 
  (rule-application 
    (fn [cell neighbour-cells]
      (if (<= cell 0)
        cell
        (let [flashing-neighbour-cells (filter #(= % -1) neighbour-cells)]
          (+ cell (count flashing-neighbour-cells)))))))

;; Grid transform: mark flashing cells as flashed (-1 -> 0)
(def mark-flashed 
  (rule-application (fn [cell _]  (if (= cell -1) 0 cell))))


;; Grid transform: repeatedly flash until no more changes occur
(def flashing-process 
  (repeated-until-first-cycle 
    (comp mark-flashed 
          increase-energy-level-from-flashing-neighbours
          mark-flashing)))                   

;; Grid transform: a single step consists of increasing the energy level and doing the flashing
(def step (comp flashing-process
                 increase-energy-level))

;; To solve part 2 apply the steps until a cycle is detected 
(def part-2 (->> (repeatedly-until-first-cycle grid step)
                 count
                 ;; the result includes the initial grid                
                 dec))
  
part-2  
