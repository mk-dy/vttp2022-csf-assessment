import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Order } from '../models';
import { PizzaService } from '../pizza.service';

const SIZES: string[] = [
  "Personal - 6 inches",
  "Regular - 9 inches",
  "Large - 12 inches",
  "Extra Large - 15 inches"
]

const PizzaToppings: string[] = [
    'chicken', 'seafood', 'beef', 'vegetables',
    'cheese', 'arugula', 'pineapple'
]

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {


  email: string = ''

  @ViewChild('size')
  size!: ElementRef;

  pizzaSize = SIZES[0]

  orderForm!: FormGroup
  toppingsGroup!: FormGroup

  constructor(private fb: FormBuilder, private pizzaSvc: PizzaService, private router: Router, private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.orderForm = this.createForm()
  }

  updateSize(size: string) {
    this.pizzaSize = SIZES[parseInt(size)]
  }

  createForm() {
    this.toppingsGroup = this.createToppings()
    return this.fb.group({
      name: this.fb.control<string>('', Validators.required),
      email: this.fb.control<string>('', [ Validators.required, Validators.email ]),
      base: this.fb.control<string>('', Validators.required),
      sauce: this.fb.control<string>('', Validators.required),
      toppings: this.toppingsGroup,
      comments: this.fb.control<string>('')      
    })
  }

  createToppings() {
    return this.fb.group({
      chicken: this.fb.control<string>(''),
      seafood: this.fb.control<string>(''),
      beef: this.fb.control<string>(''),
      vegetables: this.fb.control<string>(''),
      cheese: this.fb.control<string>(''),
      arugula: this.fb.control<string>(''),
      pineapple: this.fb.control<string>('')
    },{ validators: Validators.min(1) })
  }

  processForm() {
    const data = this.orderForm.value
    console.info('>>>> size: ', this.size.nativeElement.value) // size
    console.info('>>>> order added: ', data)
    
    let formToppings = data.toppings
    let toppings: string[] = []
    console.info('>>> toppings', data.toppings) 
    for(let key in formToppings) {
      if(formToppings[key] == true) {
        toppings.push(key);
      }
    }

    data['size'] = Number(this.size.nativeElement.value)
    data['toppings'] = toppings
    if (data['base'] === 'thick') {
      data['base'] = true
    } else {
      data['base'] = false
    }

    let order = data as Order
    console.info(order)
    this.pizzaSvc.createOrder(order)
    
    this.email = data.email
    this.router.navigate(['/orders', this.email])
  }

  onEmailClick() {
    const data = this.orderForm.value
    this.email = data.email
    this.router.navigate(['/orders', this.email])
  }

  

}
