import React from "react";
import PageTitle from "./PageTitle";

export default function About() {
  const h3Style = "text-lg font-semibold text-primary dark:text-light mb-2";
  const pStyle = "text-gray-600 dark:text-lighter";

  return (
    <div className="max-w-[1152px] min-h-[852px] mx-auto px-6 py-8 font-primary">
      <PageTitle title="About Us" />
      {/* About Us Content */}
      <p className="leading-6 mb-8 text-gray-600 dark:text-lighter">
        <span className="text-lg font-semibold text-primary dark:text-light">
          67's Sticker Store
        </span>{" "}
        store is an initiative by{" "}
        <span className="text-lg font-semibold text-primary dark:text-light">
          Designs by nmquys
        </span>
        , made to bring you the most skibidi-approved, eye-catching stickers and
        posters on the internet.
      </p>

      {/* Why Choose Us Section */}
      <h2 className="text-2xl leading-[32px] font-bold text-primary dark:text-light mb-6">
        Why Stick With Us?
      </h2>

      {/* Features */}
      <div className="space-y-8">
        {/* Feature: Premium Quality */}
        <div>
          <h3 className={h3Style}>Premium Quality (No Cap)</h3>
          <p className={pStyle}>
            We don’t do mid. Every vinyl sticker is crafted with care,
            precision, and top-tier materials for maximum drip and long-lasting
            vibes.
          </p>
        </div>

        {/* Feature: Next-Level Stickers */}
        <div>
          <h3 className={h3Style}>Next-Level Stickers</h3>
          <p className={pStyle}>
            Matte or glossy finish? You choose. Our stickers are laminated,
            scratch-resistant, weatherproof, and powered by advanced adhesive
            tech—strong enough to stay put, gentle enough to not wreck your
            laptop. Clean peel, zero drama.
          </p>
        </div>

        {/* Feature: Service That Slaps */}
        <div>
          <h3 className={h3Style}>Service That Slaps</h3>
          <p className={pStyle}>
            Your satisfaction is the main character here. We’re locked in on
            giving you a smooth, stress-free shopping experience from checkout
            to unboxing.
          </p>
        </div>

        {/* Feature: Designs That Go Hard */}
        <div>
          <h3 className={h3Style}>Designs That Go Hard</h3>
          <p className={pStyle}>
            With 1,000+ designs, we’ve got everything from relatable chaos to
            seriously funny, brain-rot-approved art. And trust—this is only the
            warm-up. More drops coming soon.
          </p>
        </div>
      </div>
    </div>
  );
}
