(ns asg.styles.typography
  (:require [cljs-styled-components.reagent :as styled
             :refer-macros [defstyled]]
            [clojure.string :as str]))

(def options {:headerFontFamily ["itc-avant-garde-gothic-pro" "sans-serif"]
              :headerWeight     "400"
              :bodyFontFamily   ["sofia-pro" "sans-serif"]
              :background       "#6c82ff"})

(defstyled Link :a
  {:text-decoration "none"})

(defstyled Navlink Link
  {:color          "white"
   :font-family    (->> options :headerFontFamily (str/join ", "))
   :letter-spacing "0.15em"
   :margin-left    "2em"
   :text-transform "uppercase"})

(defn text-stripper [{:keys [colors step diagonal]
                      :or {diagonal true}}]
  (str/join ", "
            (map-indexed (fn [index color]
                           (str "-" (* (+ index 1) step) "em "
                                (if diagonal (str (* (+ index 1) step) "em ") " 0 ")
                                "0 " color))
                         colors)))

(defn section-header [{:keys [text id]}]
  [:h1 {:id    id
        :style {:font-family   "super_plumber_brothersregular"
                :font-size     "130px"
                :text-shadow   "3px 12px black"
                :border-right  "3px solid #0e0112"
                :border-bottom "3px solid #0e0112"
                :border-top    "3px solid #ebc7d6"
                :border-left   "3px solid #ebc7d6"
                :background    "#d84900"
                :color         "#f2c5bd"
                :padding       "50px"}} text])
