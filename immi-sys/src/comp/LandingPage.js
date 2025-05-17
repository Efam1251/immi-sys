import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "../css/LandingPage.css";
import MessagePopup from "../comp/MessagePopup";

function LandingPage() {
  const [message, setMessage] = useState("");
  const [isPopupOpen, setPopupOpen] = useState(false);

  useEffect(() => {
    const sections = document.querySelectorAll(".fade-section");

    const observer = new IntersectionObserver(
      (entries, observer) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add("section-fade-in");
            observer.unobserve(entry.target);
          }
        });
      },
      { threshold: 0.5 }
    );

    sections.forEach((section) => observer.observe(section));
  }, []);

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    message: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch("/api/contact/email-contact", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        setMessage("Your message has been sent!"); // Store response message
        setPopupOpen(true);
        setFormData({ name: "", email: "", message: "" });
      } else {
        setMessage("Failed to send the message. Please try again."); // Store response message
        setPopupOpen(true);
      }
    } catch (error) {
      console.error("Error sending message:", error);
      setMessage("An error occurred. Please try again."); // Store response message
      setPopupOpen(true);
    }
  };

  return (
    <div className="modern-landing-page">
      <nav className="navbar">
        <div className="brand">
          <div>
            Acevedo Consulting, LLC.
            <p>Immigration & Travel Solutions</p>
          </div>
        </div>
        <ul className="nav-links">
          <li>
            <a href="/homepage">Home</a>
          </li>
          <li>
            <a href="#services">Services</a>
          </li>
          <li>
            <a href="#about">About</a>
          </li>
          <li>
            <a href="#contact">Contact</a>
          </li>
          <li>
            <a href="/login">Login</a>
          </li>
          <li>
            <a>+1 (603) 820-3041</a>
          </li>
        </ul>
      </nav>
      <header
        className="hero"
        style={{
          backgroundImage: `url(${require("../Background_01.jpg")})`,
          backgroundSize: "cover",
          backgroundRepeat: "no-repeat",
          backgroundPosition: "center",
          height: "100vh",
          color: "white",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          textAlign: "center",
        }}
      >
        <div className="hero-content">
          <h1>Embark on Your New Adventure</h1>
          <p>Personalized Immigration and Travel Assistance.</p>
          <button className="cta-button">Start Your Journey</button>
        </div>
      </header>
      <section id="services" className="services fade-section">
        <div className="service-container">
          <h2>Our Premium Services</h2>
          <div className="service-grid">
            <div className="service-card">
              <div>
                <h3>Visa Assistance</h3>
                <img
                  src="/img/passport-image-01.png"
                  alt="Visa Assistance"
                  className="service-image"
                />
              </div>
              <p>
                We provide expert visa assistance for various visa types,
                ensuring a smooth application process. Whether you need a
                family-based visa, travel visa, work visa, or student visa, our
                team is here to guide you through each step, handling all
                paperwork and offering personalized support.
              </p>
              <ul>
                <li>- Family-Based Visas</li>
                <li>- Travel Visas</li>
                <li>- Work Visas</li>
                <li>- Student Visas</li>
                <li>- Business Visas</li>
                <li>- Temporary Visas</li>
                <li>- Immigrant Visas</li>
                <li>- Visitor Visas</li>
                <li>- Fiancé/Fiancée Visas</li>
              </ul>
            </div>
            <div className="service-card">
              <h3>Custom Travel Plans</h3>
              <img
                src="/img/travel-01.jpeg"
                alt="Custom Travel Plans"
                className="service-image"
              />
              <p>
                We create personalized travel itineraries tailored to your
                unique preferences. Whether you're planning a relaxing vacation,
                an adventure, or a cultural exploration, our experts work with
                you to design the perfect journey that aligns with your dreams
                and expectations.
              </p>
              <ul>
                <li>- Custom Itineraries</li>
                <li>- Luxury Travel</li>
                <li>- Adventure Travel</li>
                <li>- Cultural and Heritage Tours</li>
                <li>- Family Vacations</li>
                <li>- Romantic Getaways</li>
                <li>- Group Travel</li>
                <li>- Destination Weddings</li>
                <li>- Weekend Getaways</li>
              </ul>
            </div>
            <div className="service-card">
              <h3>Translations & Similar Services</h3>
              <img
                src="/img/Document-translation.jpg"
                alt="Translations & Similar Services"
                className="service-image"
              />
              <p>
                We offer comprehensive translation and related services to meet
                your diverse language needs. Whether for personal or
                professional use, our experienced translators ensure accuracy
                and cultural sensitivity in every project. From document
                translation to certified language services, we've got you
                covered.
              </p>
              <ul>
                <li>Document Translation</li>
                <li>Certified Translations</li>
                <li>Legal Translation</li>
                <li>Technical Translation</li>
                <li>Medical Translation</li>
                <li>Interpretation Services</li>
                <li>Localization Services</li>
                <li>Subtitling & Transcription</li>
                <li>Multilingual Content Creation</li>
              </ul>
            </div>
          </div>
        </div>
      </section>
      <section id="about" className="about fade-section">
        <div className="about-container">
          <h2>About Us</h2>
          <p>
            With years of experience, our mission is to make your dreams of
            travel, immigration, and global opportunities a reality. We offer
            reliable and personalized services, ensuring that each step of your
            journey is smooth and stress-free. Whether you’re applying for a
            visa, planning your next trip, or needing expert translations, our
            dedicated team is here to support you every step of the way.
          </p>
        </div>
      </section>
      <section id="contact" className="contact fade-section">
        <div className="contact-container">
          <h2>Contact Us</h2>
          <p>
            We’d love to hear from you. Fill out the form below and we’ll get
            back to you soon.
          </p>
          <form onSubmit={handleSubmit} className="contact-form">
            <input
              type="text"
              name="name"
              placeholder="Your Name"
              value={formData.name}
              onChange={handleChange}
              required
            />
            <input
              type="email"
              name="email"
              placeholder="Your Email"
              value={formData.email}
              onChange={handleChange}
              required
            />
            <textarea
              name="message"
              placeholder="Your Message"
              rows="5"
              value={formData.message}
              onChange={handleChange}
              required
            ></textarea>
            <button type="submit" className="submit-button">
              Send Message
            </button>
          </form>
        </div>
      </section>
      <footer className="footer">
        <p>© 2025 Immigration & Travel Solutions. All rights reserved.</p>
      </footer>
      <MessagePopup
          message={message}
          isPopupOpen={isPopupOpen}
          setPopupOpen={setPopupOpen}
        />
    </div>
  );
}

export default LandingPage;
