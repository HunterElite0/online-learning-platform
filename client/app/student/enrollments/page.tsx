"use client";

import { EnrollmentCardStud } from "@/components/enrollment-card-stud";
import Link from "next/link";
import { useEffect, useState } from "react";

async function fetchEnrollments(token: string) {
  const URL = "http://localhost:8080/learning/enrollment/student-list";
  // const URL = "http://course-service:8080/learning/enrollment/student-list/";
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
      <header className="py-3 border-b-4">
        <div className="flex items-center justify-between">
          <Link className="flex items-center gap-2" href="/student">
            <ArrowLeftIcon className="w-5 h-5" />
            <h1 className="text-2xl font-bold">Student Dashboard</h1>
          </Link>
        </div>
      </header>
      <section className="container mx-auto py-8 px-4 md:px-6 lg:px-8">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold">View Enrollments</h2>
        </div>
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
          {enrollments.length > 0 ? (
            enrollments.map((enrollment: any) => (
              <EnrollmentCardStud
                key={enrollment.id}
                courseId={enrollment.courseId}
                enrollmentId={enrollment.id}
                status={enrollment.status}
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
function ArrowLeftIcon(props: any) {
  return (
    <svg
      {...props}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
    >
      <path d="m12 19-7-7 7-7" />
      <path d="M19 12H5" />
    </svg>
  );
}
