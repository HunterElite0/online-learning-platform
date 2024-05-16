import { AvatarImage, AvatarFallback, Avatar } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import {
  DropdownMenuTrigger,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuItem,
  DropdownMenuContent,
  DropdownMenu,
} from "@/components/ui/dropdown-menu";
import Link from "next/link";
import { cookies } from "next/headers";

async function getAccountDetails(id: number) {
  // const URL : string = "http://account-service:8081/account/user/" + id;
  const URL: string = "http://localhost:8081/account/user/" + id;
  try {
    const res = await fetch(URL);
    const response = await res.json();
    const accountDetails: any = response;
    // console.log(accountDetails);
    return accountDetails.name;
  } catch (error) {
    return null;
  }
}

function getInitials(name: string) {
  const initials = name
    .match(/(\b\S)?/g)
    ?.join("")
    ?.match(/(^\S|\S$)?/g)
    ?.join("");
  return initials || "N/A";
}

export default async function StudentPage() {
  const cookieStore = cookies();
  const id = cookieStore.get("id")?.value;
  if (!id) {
    return <div>Not logged in</div>;
  }
  const accountDetails: string = await getAccountDetails(
    id as unknown as number
  );
  if (!accountDetails) {
    return <div>Failed to fetch account</div>;
  }
  const initials = getInitials(accountDetails);

  return (
    <main className="flex flex-col min-h-screen">
      <header className="bg-gray-950 text-white py-4 px-6">
        <div className="flex items-center justify-between">
          <h1 className="text-2xl font-bold">Student Dashboard</h1>
          <div className="flex items-center gap-4">
            <DropdownMenu>
              <DropdownMenuTrigger asChild>
                <Button className="rounded-full" size="icon" variant="ghost">
                  <Avatar>
                    <AvatarImage alt="@shadcn" src="/placeholder-avatar.jpg" />
                    <AvatarFallback>{initials}</AvatarFallback>
                  </Avatar>
                  <span className="sr-only">Toggle user menu</span>
                </Button>
              </DropdownMenuTrigger>
              <DropdownMenuContent align="end">
                <DropdownMenuLabel>{accountDetails}</DropdownMenuLabel>
                <DropdownMenuSeparator />
                <DropdownMenuItem>Account Settings</DropdownMenuItem>
                <DropdownMenuItem>Logout</DropdownMenuItem>
              </DropdownMenuContent>
            </DropdownMenu>
          </div>
        </div>
      </header>
      <section className="container mx-auto py-8 px-4 md:px-6 lg:px-8 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-6">
        <Link
          className="bg-white shadow-md rounded-lg p-4 flex flex-col items-center justify-center gap-2 hover:bg-gray-100 transition-colors"
          href="/student/courses"
        >
          <BookOpenIcon className="w-8 h-8 text-gray-600" />
          <span className="text-gray-700 font-medium">ALL COURSES</span>
        </Link>
        <Link
          className="bg-white shadow-md rounded-lg p-4 flex flex-col items-center justify-center gap-2 hover:bg-gray-100 transition-colors"
          href="/student/notifications"
        >
          <BellIcon className="w-8 h-8 text-gray-600" />
          <span className="text-gray-700 font-medium">NOTIFICATIONS</span>
        </Link>
        <Link
          className="bg-white shadow-md rounded-lg p-4 flex flex-col items-center justify-center gap-2 hover:bg-gray-100 transition-colors"
          href="/student/enrollments"
        >
          <ClipboardListIcon className="w-8 h-8 text-gray-600" />
          <span className="text-gray-700 font-medium">VIEW ENROLLMENTS</span>
        </Link>
      </section>
    </main>
  );
}

function BellIcon(props: any) {
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
      <path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9" />
      <path d="M10.3 21a1.94 1.94 0 0 0 3.4 0" />
    </svg>
  );
}

function BookIcon(props: any) {
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
      <path d="M4 19.5v-15A2.5 2.5 0 0 1 6.5 2H20v20H6.5a2.5 2.5 0 0 1 0-5H20" />
    </svg>
  );
}

function BookOpenIcon(props: any) {
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
      <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" />
      <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" />
    </svg>
  );
}

function ClipboardListIcon(props: any) {
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
      <rect width="8" height="4" x="8" y="2" rx="1" ry="1" />
      <path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2" />
      <path d="M12 11h4" />
      <path d="M12 16h4" />
      <path d="M8 11h.01" />
      <path d="M8 16h.01" />
    </svg>
  );
}
