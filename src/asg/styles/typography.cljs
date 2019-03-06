(ns asg.styles.typography
  (:require [cljs-styled-components.reagent :as styled
             :refer-macros [defstyled]]
            [clojure.string :as str]))

(def options {:headerFontFamily ["itc-avant-garde-gothic-pro" "sans-serif"]
              :headerWeight "400"
              :bodyFontFamily ["sofia-pro" "sans-serif"]})

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

(defn section-header [section-text]
  [:h1 {:id (str/lower-case section-text)} section-text])
