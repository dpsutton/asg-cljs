(ns asg.stripes
  (:require [cljs-styled-components.reagent :refer [defstyled clj-props]]
            [asg.styles.colors :as colors]))

(defstyled StripesContainer :div
  {:position "relative"
   :height   "25vw"
   :overflow "hidden"
   :width    "100%"
   :z-Index   "-1"})

(defstyled ClippingPath :div
  {:position   "relative"
   :margin-top "-20vw"})

(defstyled StripeSVG :svg
  {:position "absolute"
   :top (clj-props #(str (* (:index %) 10) "px"))
   :width "100%"
   :height "40vw"
   :z-index (clj-props #(str "-" (:index %)))})

(defn makeStripe [{:keys [color index]}]
  [StripeSVG {:clj                 {:index index}
              :xmlns               "http://www.w3.org/2000/svg"
              :viewBox             "0 0 100 200"
              :preserveAspectRatio "none"}
   [:polygon {:fill   color
              :points "0,0 0,200 100,110 100,0"}]])

(defn MainStripe []
  [StripesContainer
   (into [ClippingPath]
         (map-indexed (fn [i color]
                        (makeStripe {:color color
                                     :index i})))
         (map colors/stripes [:blue :white :yellow :red :blue]))])
