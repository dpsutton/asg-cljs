(ns asg.main
  (:require [reagent.core :as r]
            [asg.styles.alignment :as align]
            [asg.styles.typography :as typo]
            [asg.header :as header]
            [asg.sections.banner :as banner]
            [asg.sections.events :as events]
            [asg.sections.slack-signup :as slack]
            [asg.stripes :as stripes]
            [clojure.string :as str]
            ["typography" :as typography]
            ["react-typography" :as rtypo]))

(defn cloud [side]
  [:img {:src   "/images/mario-clouds.png"
         :style {:position "absolute"
                 :bottom   (str (min 200 (- 500 (rand-int 1000))) "px")
                 side     "10px"
                 :z-index  "-100"}}])

(defn asg-page []
  [:div
   [rtypo/TypographyStyle #js{:typography (typography (clj->js typo/options))}]
   [rtypo/GoogleFont #js{:typography (typography (clj->js typo/options))}]
   [header/Header]
   [banner/Banner]
   [stripes/MainStripe]
   [events/Events {:max-events 10}]
   [slack/Signup]
   [cloud :right]
   [cloud :left]])

(defn main! []
  (r/render [asg-page]
            (js/document.getElementById "app")))

(defn ^:dev/after-load reload! []
  (main!))
