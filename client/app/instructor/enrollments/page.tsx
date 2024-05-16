"use client";

import { EnrollmentCard } from "@/components/enrollment-card";
import { useEffect, useState } from "react";

async function fetchEnrollments(token: string) {
  const URL = "http://localhost:8080/learning/enrollment/instructor-list";
  // const URL = "http://course-service:8080/learning/enrollment/instructor-list/";
  const response = await fetch(URL, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      jwt: token,
    },
  });
  if (response.ok) {
    const enrollments = await response.json();
    return enrollments;
  }
  // console.log(response);
  return [];
}

const handleAccept = async (enrollmentId: number) => {
  const URL = "http://localhost:8080/learning/enrollment/accept/";
  // const URL = "http://course-service:8080/learning/enrollment/accept/";

  const jwt = require("js-cookie").get("jwt");
  const response = await fetch(URL + enrollmentId, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      jwt: jwt,
    },
  });
  if (response.ok) {
    alert("Enrollment accepted");
    window.location.href = "/instructor/enrollments";
  } else {
    alert("Failed to accept enrollment");
  }
};

const handleReject = async (enrollmentId: number) => {
  const URL = "http://localhost:8080/learning/enrollment/reject/";
  // const URL = "http://course-service:8080/learning/enrollment/reject/";

  const jwt = require("js-cookie").get("jwt");
  const response = await fetch(URL + enrollmentId, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      jwt: jwt,
    },
  });
  if (response.ok) {
    alert("Enrollment rejected");
    window.location.href = "/instructor/enrollments";
  } else {
    alert("Failed to reject enrollment");
  }
};

export default function EnrollmentsPage() {
  const [enrollments, setEnrollments] = useState([]);
  const [loading, setLoading] = useState(true);
  const cookies = require("js-cookie");

  useEffect(() => {
    const fetchData = async () => {
      const token = cookies.get("jwt");
      if (token) {
        const enrollmentsData = await fetchEnrollments(token);
        setEnrollments(enrollmentsData);
      }
      setLoading(false);
    };

    fetchData();
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <main className="flex flex-col min-h-screen bg-gray-950 text-white">
      <section className="container mx-auto py-8 px-4 md:px-6 lg:px-8">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold">Manage Enrollments</h2>
        </div>
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
          {enrollments.length > 0 ? (
            enrollments.map((enrollment: any) => (
              <EnrollmentCard
                key={enrollment.id}
                courseId={enrollment.courseId}
                studentId={enrollment.studentId}
                onAccept={() => handleAccept(enrollment.id)}
                onReject={() => handleReject(enrollment.id)}
                enrollmentId={enrollment.id}
              />
            ))
          ) : (
            <p className="text-center text-gray-400">No enrollments found.</p>
          )}
        </div>
      </section>
    </main>
  );
}
