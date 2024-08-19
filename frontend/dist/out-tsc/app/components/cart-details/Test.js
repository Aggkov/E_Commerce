import { Subject } from 'rxjs';
function main() {
    const subject = new Subject();
    subject.subscribe(value => console.log(`Subscriber 1: ${value}`));
    subject.next(1); // Subscriber 1: 1
    subject.next(2); // Subscriber 1: 2
}
// Call the main function to run the test
main();
//# sourceMappingURL=Test.js.map