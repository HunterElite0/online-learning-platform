import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";

const EnrollmentCardStud = ({
  enrollmentId,
  courseId,
  status,
}: {
  enrollmentId: number;
  courseId: number;
  status: string;
}) => {
  return (
    <Card>
      <div className="flex items-center justify-between w-full h-20 py-6 px-3">
        <div className="flex items-center gap-2 pr-4">
          <p>Enrollment for course: {courseId}</p>
          <p>Status: {status}</p>
        </div>
      </div>
    </Card>
  );
};

export { EnrollmentCardStud };
