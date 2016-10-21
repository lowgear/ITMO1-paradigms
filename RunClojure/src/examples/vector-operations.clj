(chapter "Vector and Matrix Operations")

(section "Vector Operations")

(defn v [op] (partial (partial mapv op)))
(def v+ "Pointwise vector addition" (v +))
(def v- "Pointwise vector subtraction" (v -))
(def v* "Pointwise vector multiplication" (v *))
(def scalar "Scalar product" (comp (partial apply +) v*))

(example '(v+ [1 2 3] [4 5 6] [7 8 9]))
(example '(v- [7 8 9] [4 5 6] [1 2 3]))
(example '(v* [1 2 3] [4 5 6] [7 8 9]))
(example '(scalar [1 2 3] [4 5 6]))

(section "Matrix Operations")
(def m+ "Pointwise matrix addition" (v v+))
(def m- "Pointwise matrix subtraction" (v v-))
(def m* "Pointwise matrix multiplication" (v v*))
(def transpose "Matrix transposition" (partial apply mapv vector))
(defn m** "Matrix multiplication" [ma mb]
    (mapv (fn [ra] (mapv (partial scalar ra) (transpose mb))) ma))

(example '(m+ [[1 2] [3 4]] [[5 6] [7 8]]))
(example '(m- [[1 2] [3 4]] [[5 6] [7 8]]))
(example '(m* [[1 2] [3 4]] [[5 6] [7 8]]))
(example '(transpose [[1 2] [3 4]]))
(example '(m** [[1 2 3] [4 5 6]] [[7 8] [9 10] [11 12]]))
